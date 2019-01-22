/*************************************************************************
 *
 *  Copyright (c) [2009] - [2019] Inventivelink
 *  All Rights Reserved.
 *
 ************************************************************************/

package inventivelink.com.fb2sql;

import android.support.annotation.NonNull;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import fr.heymum.yoomum.bo.Child;
import fr.heymum.yoomum.bo.Mum;
import fr.heymum.yoomum.bo.MumGeoLocation;

import static inventivelink.com.fb2sql.TestCommon.connectSQLDatabase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


@RunWith(AndroidJUnit4.class)
public class SQLDatabaseInstrumentedCRUDMum {

    private int inProgressCount = 0;

    private synchronized void incTestsCount(int value) {
        inProgressCount += value;
    }

    private synchronized int getTestsCount() {
        return inProgressCount;
    }


    @Before
    public void commonBeforeAll() {
        connectSQLDatabase();
    }

    @Test
    public void crudMUM() {
        incTestsCount(1);
        final Mum m = new Mum();
        final String key = SQLDatabase.getInstance().generateKey();
        m.setMumId(key);


        Child c1 = new Child();
        c1.setName("Samuel");
        c1.setChildId(SQLDatabase.getInstance().generateKey());

        Child c2 = new Child();
        c2.setName("Tristan");
        c2.setChildId(SQLDatabase.getInstance().generateKey());


        List children =  new ArrayList<Child>();
        children.add(c1);
        children.add(c2);
        m.setChildren(children);

        MumGeoLocation l = new MumGeoLocation();
        m.setLocation(l);

        SQLDatabase.getInstance().getReference("mums").setValue(m).addOnCompleteListener(new OnCompleteListener<SQLDatabaseSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<SQLDatabaseSnapshot> task) {
                if (!task.isSuccessful())
                    task.getException().printStackTrace();
                assertEquals(task.isSuccessful(),true);
                // R
                SQLDatabase.getInstance().getReference("mums").child(key).addListenerForSingleValueEvent(new SQLValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull SQLDatabaseSnapshot var1) {
                        Mum e1 = var1.getValue(Mum.class);
                        assertEquals(m.getChildren().size(),e1.getChildren().size());
                        e1.setDateOfBirth(null);
                        e1.setLastConnectionTimestamp(null);


                        assertEquals(m.getMumId(),e1.getMumId());
                        e1.getChildren().get(0).setDateOfBirth(null);
                        e1.getChildren().get(1).setDateOfBirth(null);

                        e1.setPhoto("http://someurl");

                        // U
                        SQLDatabase.getInstance().getReference("mums").child(key).setValue(e1).addOnCompleteListener(new OnCompleteListener<SQLDatabaseSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<SQLDatabaseSnapshot> task) {
                                assertEquals(task.isSuccessful(),true);
                                // D
                                SQLDatabase.getInstance().getReference("mums").child(key).setValue(null).addOnCompleteListener(new OnCompleteListener<SQLDatabaseSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<SQLDatabaseSnapshot> task) {
                                        SQLDatabaseLogger.info(m);
                                        assertEquals(task.isSuccessful(),true);
                                        incTestsCount(-1);
                                    }
                                });
                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull SQLDatabaseException var1) {
                        assertEquals(true,false);
                    }
                });
            }
        });

    }

    @After
    public void after() throws Exception{
        while (getTestsCount() > 0) {
            Thread.sleep(1000);
        }
    }

}
