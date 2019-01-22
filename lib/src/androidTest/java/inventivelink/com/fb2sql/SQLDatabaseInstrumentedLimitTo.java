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
public class SQLDatabaseInstrumentedLimitTo {

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
    public void limitTo() {
        incTestsCount(1);
        SQLDatabase.getInstance().getReference("mums").limitToFirst(10).addListenerForSingleValueEvent(new SQLValueEventListener() {
            @Override
            public void onDataChange(@NonNull SQLDatabaseSnapshot s) {
                Iterable<SQLDatabaseSnapshot> mums = s.getChildren();
                int count = 0;
                for (SQLDatabaseSnapshot mum : mums) {
                    count ++;
                }
                assertEquals(count,10);
                incTestsCount(-1);
            }
            @Override
            public void onCancelled(@NonNull SQLDatabaseException e) {
                SQLDatabaseLogger.error(e);
                assertEquals(true,false);
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
