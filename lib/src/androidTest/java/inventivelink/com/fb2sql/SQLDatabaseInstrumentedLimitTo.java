/*************************************************************************
 *
 *  Copyright (c) [2009] - [2019] Inventivelink
 *  All Rights Reserved.
 *
 ************************************************************************/

package inventivelink.com.fb2sql;

import android.support.annotation.NonNull;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

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
            public void onDataChange(@NonNull SQLDataSnapshot s) {
                Iterable<SQLDataSnapshot> mums = s.getChildren();
                int count = 0;
                for (SQLDataSnapshot mum : mums) {
                    count ++;
                }
                assertEquals(count,10);
                incTestsCount(-1);
            }
            @Override
            public void onCancelled(@NonNull SQLDatabaseError e) {
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
