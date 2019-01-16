package inventivelink.com.fb2sql;

import android.support.annotation.NonNull;
import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.database.DatabaseException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Iterator;

import fr.heymum.yoomum.bo.Address;
import fr.heymum.yoomum.bo.Child;
import fr.heymum.yoomum.bo.Comment;
import fr.heymum.yoomum.bo.Event;
import fr.heymum.yoomum.bo.FeedItem;
import fr.heymum.yoomum.bo.Mum;

import static inventivelink.com.fb2sql.ymentities.YMNodeNames.CHILDREN_NODE;
import static inventivelink.com.fb2sql.ymentities.YMNodeNames.EVENTS_NODE;
import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class SQLDatabaseInstrumentedTestYM {

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
    public void event() {
        incTestsCount(1);
        SQLDatabase.getInstance().getReference("events").child("1").addListenerForSingleValueEvent(new SQLValueEventListener() {
            @Override
            public void onDataChange(@NonNull SQLDataSnapshot s) {
                Event e = s.getValue(Event.class);
                SQLDatabaseLogger.Logi(e);
                assertEquals(false,e == null);
                incTestsCount(-1);
            }
            @Override
            public void onCancelled(@NonNull SQLDatabaseError e) {
                SQLDatabaseLogger.Loge(e);
                assertEquals(true,false);
            }
        });
    }

    @Test
    public void children() {
        incTestsCount(1);
        SQLDatabase.getInstance().getReference("childrens").child("1").addListenerForSingleValueEvent(new SQLValueEventListener() {
            @Override
            public void onDataChange(@NonNull SQLDataSnapshot s) {
                Child e = s.getValue(Child.class);
                SQLDatabaseLogger.Logi(e);
                assertEquals(false,e == null);
                incTestsCount(-1);
            }
            @Override
            public void onCancelled(@NonNull SQLDatabaseError e) {
                SQLDatabaseLogger.Loge(e);
                assertEquals(true,false);
            }
        });
    }


    @Test
    public void event_comments() {
        incTestsCount(1);
        SQLDatabase.getInstance().getReference("event_comments").child("1").addListenerForSingleValueEvent(new SQLValueEventListener() {
            @Override
            public void onDataChange(@NonNull SQLDataSnapshot s) {
                Comment e = s.getValue(Comment.class);
                SQLDatabaseLogger.Logi(e);
                assertEquals(false,e == null);
                incTestsCount(-1);
            }
            @Override
            public void onCancelled(@NonNull SQLDatabaseError e) {
                SQLDatabaseLogger.Loge(e);
                assertEquals(true,false);
            }
        });
    }


    @Test
    public void feed() {
        incTestsCount(1);
        SQLDatabase.getInstance().getReference("feeds").child("1").addListenerForSingleValueEvent(new SQLValueEventListener() {
            @Override
            public void onDataChange(@NonNull SQLDataSnapshot s) {
                FeedItem e = s.getValue(FeedItem.class);
                SQLDatabaseLogger.Logi(e);
                assertEquals(false,e == null);
                incTestsCount(-1);
            }
            @Override
            public void onCancelled(@NonNull SQLDatabaseError e) {
                SQLDatabaseLogger.Loge(e);
                assertEquals(true,false);
            }
        });
    }

    @Test
    public void feed_comments() {
        incTestsCount(1);
        SQLDatabase.getInstance().getReference("feed_comments").child("1").addListenerForSingleValueEvent(new SQLValueEventListener() {
            @Override
            public void onDataChange(@NonNull SQLDataSnapshot s) {
                Comment e = s.getValue(Comment.class);
                SQLDatabaseLogger.Logi(e);
                assertEquals(false,e == null);
                incTestsCount(-1);
            }
            @Override
            public void onCancelled(@NonNull SQLDatabaseError e) {
                SQLDatabaseLogger.Loge(e);
                assertEquals(true,false);
            }
        });
    }

    @Test
    public void locations() {
        incTestsCount(1);
        SQLDatabase.getInstance().getReference("locations").child("1").addListenerForSingleValueEvent(new SQLValueEventListener() {
            @Override
            public void onDataChange(@NonNull SQLDataSnapshot s) {
                Address e = s.getValue(Address.class);
                SQLDatabaseLogger.Logi(e);
                assertEquals(false,e == null);
                incTestsCount(-1);
            }
            @Override
            public void onCancelled(@NonNull SQLDatabaseError e) {
                SQLDatabaseLogger.Loge(e);
                assertEquals(true,false);
            }
        });
    }

    @Test
    public void locations_comments() {
        incTestsCount(1);
        SQLDatabase.getInstance().getReference("locations_comments").child("1").addListenerForSingleValueEvent(new SQLValueEventListener() {
            @Override
            public void onDataChange(@NonNull SQLDataSnapshot s) {
                Comment e = s.getValue(Comment.class);
                SQLDatabaseLogger.Logi(e);
                assertEquals(false,e == null);
                incTestsCount(-1);
            }
            @Override
            public void onCancelled(@NonNull SQLDatabaseError e) {
                SQLDatabaseLogger.Loge(e);
                assertEquals(true,false);
            }
        });
    }

    @Test
    public void mums() {
        incTestsCount(1);
        SQLDatabase.getInstance().getReference("mums").child("1").addListenerForSingleValueEvent(new SQLValueEventListener() {
            @Override
            public void onDataChange(@NonNull SQLDataSnapshot s) {
                Mum e = s.getValue(Mum.class);
                SQLDatabaseLogger.Logi(e);
                assertEquals(false,e == null);
                incTestsCount(-1);
            }
            @Override
            public void onCancelled(@NonNull SQLDatabaseError e) {
                SQLDatabaseLogger.Loge(e);
                assertEquals(true,false);
            }
        });
    }


    @Test
    public void allmums() {
        incTestsCount(1);
        SQLDatabase.getInstance().getReference("mums").addListenerForSingleValueEvent(new SQLValueEventListener() {
            @Override
            public void onDataChange(@NonNull SQLDataSnapshot s) {
                Iterable<SQLDataSnapshot> mums = s.getChildren();
                for (SQLDataSnapshot mum : mums) {
                    Mum e = mum.getValue(Mum.class);
                    SQLDatabaseLogger.Logi(e);
                    assertEquals(false,e == null);
                }
                incTestsCount(-1);
            }
            @Override
            public void onCancelled(@NonNull SQLDatabaseError e) {
                SQLDatabaseLogger.Loge(e);
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
