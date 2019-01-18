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
        SQLDatabase.getInstance().setEndPoint("http://192.168.1.99:8000/api","test","test",3,10,10,30);
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
        List children =  new ArrayList<Child>();
        children.add(c1);
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
                        assertEquals(m.getMumId(),e1.getMumId());
                        assertEquals(m.getChildren().size(),e1.getChildren().size());

                        // U
                        m.setChildren(null);
                        SQLDatabase.getInstance().getReference("mums").child(key).setValue(m).addOnCompleteListener(new OnCompleteListener<SQLDatabaseSnapshot>() {
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

    @Test
    public void geoSearchMum() {
        incTestsCount(1);
        final Mum venceMum = new Mum();
        final String key = SQLDatabase.getInstance().generateKey();
        venceMum.setMumId(key);
        MumGeoLocation l = new MumGeoLocation();
        l.setLatitude(43.716667);
        l.setLongitude(7.116667);
        venceMum.setLocation(l);

        SQLDatabase.getInstance().getReference("mums").setValue(venceMum).addOnCompleteListener(new OnCompleteListener<SQLDatabaseSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<SQLDatabaseSnapshot> task) {
                if (!task.isSuccessful())
                    task.getException().printStackTrace();
                assertEquals(task.isSuccessful(),true);
                // shouldl find one
                SQLDatabase.getInstance().getReference("mums").withinPerimeter(venceMum.getLocation().getLatitude(),venceMum.getLocation().getLongitude(),10,"km").addListenerForSingleValueEvent(new SQLValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull SQLDatabaseSnapshot s) {
                        Iterable<SQLDatabaseSnapshot> mums = s.getChildren();
                        int count = 0;
                        for (SQLDatabaseSnapshot mum : mums) {
                            Mum e = mum.getValue(Mum.class);
                            assertEquals(false,e.getLocation() == null);
                            count ++;
                        }
                        assertNotEquals(count,0);
                        // shouldl not find one
                        incTestsCount(1);
                        SQLDatabase.getInstance().getReference("mums").withinPerimeter(0d,0d,10,"km").addListenerForSingleValueEvent(new SQLValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull SQLDatabaseSnapshot s) {
                                Iterable<SQLDatabaseSnapshot> mums = s.getChildren();
                                int count = 0;
                                for (SQLDatabaseSnapshot mum : mums) {
                                    count ++;
                                }
                                assertEquals(count,0);
                                incTestsCount(-1);
                            }
                            @Override
                            public void onCancelled(@NonNull SQLDatabaseException e) {
                                SQLDatabaseLogger.error(e);
                                assertEquals(true,false);
                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull SQLDatabaseException e) {
                        SQLDatabaseLogger.error(e);
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
