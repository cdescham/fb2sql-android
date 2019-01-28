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

import fr.heymum.yoomum.bo.Mum;
import fr.heymum.yoomum.bo.MumGeoLocation;

import static inventivelink.com.fb2sql.TestCommon.connectSQLDatabase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


@RunWith(AndroidJUnit4.class)
public class SQLDatabaseInstrumentedGeosearchMum {

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
    public void geoSearchMum() {
        incTestsCount(1);
        final Mum venceMum = new Mum();
        final String key = SQLDatabase.getInstance().generateKey();
        venceMum.setMumId(key);
        MumGeoLocation l = new MumGeoLocation();
        l.setLatitude(43.716667);
        l.setLongitude(7.116667);
        venceMum.setLocation(l);

        SQLDatabase.getInstance().getReference("mums").setValue(venceMum).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful())
                    task.getException().printStackTrace();
                assertEquals(task.isSuccessful(),true);
                // shouldl find one
                SQLDatabase.getInstance().getReference("mums").queryAtLocation(venceMum.getLocation().getLatitude(),venceMum.getLocation().getLongitude(),10d,"km").addListenerForSingleValueEvent(new SQLValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull SQLDataSnapshot s) {
                        Iterable<SQLDataSnapshot> mums = s.getChildren();
                        int count = 0;
                        for (SQLDataSnapshot mum : mums) {
                            Mum e = mum.getValue(Mum.class);
                            assertEquals(false,e.getLocation() == null);
                            count ++;
                        }
                        assertNotEquals(count,0);
                        // shouldl not find one
                        incTestsCount(1);
                        SQLDatabase.getInstance().getReference("mums").queryAtLocation(0d,0d,10d,"km").addListenerForSingleValueEvent(new SQLValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull SQLDataSnapshot s) {
                                Iterable<SQLDataSnapshot> mums = s.getChildren();
                                int count = 0;
                                for (SQLDataSnapshot mum : mums) {
                                    count ++;
                                }
                                assertEquals(count,0);
                                incTestsCount(-1);
                            }
                            @Override
                            public void onCancelled(@NonNull SQLDatabaseError e) {
                                SQLDatabaseLogger.error(e);
                                assertEquals(true,false);
                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull SQLDatabaseError e) {
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
