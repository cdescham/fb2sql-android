/*************************************************************************
 *
 *  Copyright (c) [2009] - [2019] Inventivelink
 *  All Rights Reserved.
 *
 ************************************************************************/


package inventivelink.com.fb2sql;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class SQLDatabaseInstrumentedTestBasic {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("inventivelink.com.fb2sql", appContext.getPackageName());
    }

    @Test
    public void CRUD() {
        SQLDatabase.getInstance().setEndPoint("http://192.168.1.99/ym/sql.php","test","test",3,10,10,30);
        // C
        final TestEntity e = new TestEntity();
        e.setS("a");
        e.setB(true);
        e.setL(200L);
        e.setD(2.0d);
        final String key = SQLDatabase.getInstance().generateKey();
        SQLDatabase.getInstance().getReference("TEST").child(key).setValue(e).addOnCompleteListener(new OnCompleteListener<SQLDatabaseSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<SQLDatabaseSnapshot> task) {
                if (!task.isSuccessful())
                    task.getException().printStackTrace();
                assertEquals(task.isSuccessful(),true);
                // R
                SQLDatabase.getInstance().getReference("TEST").child(key).addListenerForSingleValueEvent(new SQLValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull SQLDatabaseSnapshot var1) {
                        TestEntity e1 = var1.getValue(TestEntity.class);
                        assertEquals(e,e1);
                        // U
                        e.setS("b");
                        SQLDatabase.getInstance().getReference("TEST").child(key).setValue(e).addOnCompleteListener(new OnCompleteListener<SQLDatabaseSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<SQLDatabaseSnapshot> task) {
                                assertEquals(task.isSuccessful(),true);
                                // D
                                SQLDatabase.getInstance().getReference("TEST").child(key).setValue(null).addOnCompleteListener(new OnCompleteListener<SQLDatabaseSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<SQLDatabaseSnapshot> task) {
                                        assertEquals(task.isSuccessful(),true);
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

}
