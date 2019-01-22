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

import fr.heymum.yoomum.bo.Address;
import fr.heymum.yoomum.bo.Child;
import fr.heymum.yoomum.bo.Comment;
import fr.heymum.yoomum.bo.Event;
import fr.heymum.yoomum.bo.FeedItem;
import fr.heymum.yoomum.bo.Mum;

import static inventivelink.com.fb2sql.TestCommon.connectSQLDatabase;
import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class SQLDatabaseInstrumentedTestAllMums {

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
    public void allmums() {
        incTestsCount(1);
        SQLDatabase.getInstance().getReference("mums").addListenerForSingleValueEvent(new SQLValueEventListener() {
            @Override
            public void onDataChange(@NonNull SQLDatabaseSnapshot s) {
                Iterable<SQLDatabaseSnapshot> mums = s.getChildren();
                for (SQLDatabaseSnapshot mum : mums) {
                    Mum e = mum.getValue(Mum.class);
                    SQLDatabaseLogger.info(e);
                    assertEquals(false,e == null);
                }
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
