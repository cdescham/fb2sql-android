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

import fr.heymum.yoomum.bo.Address;
import fr.heymum.yoomum.bo.Child;
import fr.heymum.yoomum.bo.Comment;
import fr.heymum.yoomum.bo.Event;
import fr.heymum.yoomum.bo.FeedItem;
import fr.heymum.yoomum.bo.Mum;

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
        SQLDatabase.getInstance().setEndPoint("http://172.20.10.2:8000/api","test","test",3,10,10,30);
    }

    @Test
    public void event() {
        incTestsCount(1);
        SQLDatabase.getInstance().getReference("events").child("1").addListenerForSingleValueEvent(new SQLValueEventListener() {
            @Override
            public void onDataChange(@NonNull SQLDatabaseSnapshot s) {
                Event e = s.getValue(Event.class);
                SQLDatabaseLogger.info(e);
                assertEquals(false,e == null);
                incTestsCount(-1);
            }
            @Override
            public void onCancelled(@NonNull SQLDatabaseException e) {
                SQLDatabaseLogger.error(e);
                assertEquals(true,false);
            }
        });
    }

    @Test
    public void children() {
        incTestsCount(1);
        SQLDatabase.getInstance().getReference("childrens").child("1").addListenerForSingleValueEvent(new SQLValueEventListener() {
            @Override
            public void onDataChange(@NonNull SQLDatabaseSnapshot s) {
                Child e = s.getValue(Child.class);
                SQLDatabaseLogger.info(e);
                assertEquals(false,e == null);
                incTestsCount(-1);
            }
            @Override
            public void onCancelled(@NonNull SQLDatabaseException e) {
                SQLDatabaseLogger.error(e);
                assertEquals(true,false);
            }
        });
    }


    @Test
    public void event_comments() {
        incTestsCount(1);
        SQLDatabase.getInstance().getReference("event_comments").child("1").addListenerForSingleValueEvent(new SQLValueEventListener() {
            @Override
            public void onDataChange(@NonNull SQLDatabaseSnapshot s) {
                Comment e = s.getValue(Comment.class);
                SQLDatabaseLogger.info(e);
                assertEquals(false,e == null);
                incTestsCount(-1);
            }
            @Override
            public void onCancelled(@NonNull SQLDatabaseException e) {
                SQLDatabaseLogger.error(e);
                assertEquals(true,false);
            }
        });
    }


    @Test
    public void feed() {
        incTestsCount(1);
        SQLDatabase.getInstance().getReference("feeds").child("1").addListenerForSingleValueEvent(new SQLValueEventListener() {
            @Override
            public void onDataChange(@NonNull SQLDatabaseSnapshot s) {
                FeedItem e = s.getValue(FeedItem.class);
                SQLDatabaseLogger.info(e);
                assertEquals(false,e == null);
                incTestsCount(-1);
            }
            @Override
            public void onCancelled(@NonNull SQLDatabaseException e) {
                SQLDatabaseLogger.error(e);
                assertEquals(true,false);
            }
        });
    }

    @Test
    public void feed_comments() {
        incTestsCount(1);
        SQLDatabase.getInstance().getReference("feed_comments").child("1").addListenerForSingleValueEvent(new SQLValueEventListener() {
            @Override
            public void onDataChange(@NonNull SQLDatabaseSnapshot s) {
                Comment e = s.getValue(Comment.class);
                SQLDatabaseLogger.info(e);
                assertEquals(false,e == null);
                incTestsCount(-1);
            }
            @Override
            public void onCancelled(@NonNull SQLDatabaseException e) {
                SQLDatabaseLogger.error(e);
                assertEquals(true,false);
            }
        });
    }

    @Test
    public void locations() {
        incTestsCount(1);
        SQLDatabase.getInstance().getReference("locations").child("1").addListenerForSingleValueEvent(new SQLValueEventListener() {
            @Override
            public void onDataChange(@NonNull SQLDatabaseSnapshot s) {
                Address e = s.getValue(Address.class);
                SQLDatabaseLogger.info(e);
                assertEquals(false,e == null);
                incTestsCount(-1);
            }
            @Override
            public void onCancelled(@NonNull SQLDatabaseException e) {
                SQLDatabaseLogger.error(e);
                assertEquals(true,false);
            }
        });
    }

    @Test
    public void locations_comments() {
        incTestsCount(1);
        SQLDatabase.getInstance().getReference("locations_comments").child("1").addListenerForSingleValueEvent(new SQLValueEventListener() {
            @Override
            public void onDataChange(@NonNull SQLDatabaseSnapshot s) {
                Comment e = s.getValue(Comment.class);
                SQLDatabaseLogger.info(e);
                assertEquals(false,e == null);
                incTestsCount(-1);
            }
            @Override
            public void onCancelled(@NonNull SQLDatabaseException e) {
                SQLDatabaseLogger.error(e);
                assertEquals(true,false);
            }
        });
    }

    @Test
    public void mums() {
        incTestsCount(1);
        SQLDatabase.getInstance().getReference("mums").child("1").addListenerForSingleValueEvent(new SQLValueEventListener() {
            @Override
            public void onDataChange(@NonNull SQLDatabaseSnapshot s) {
                Mum e = s.getValue(Mum.class);
                SQLDatabaseLogger.info(e);
                assertEquals(false,e == null);
                incTestsCount(-1);
            }
            @Override
            public void onCancelled(@NonNull SQLDatabaseException e) {
                SQLDatabaseLogger.error(e);
                assertEquals(true,false);
            }
        });
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

    @Test
    public void crudMUM() {
        incTestsCount(1);
        final Mum m = new Mum();
        final String key = SQLDatabase.getInstance().generateKey();
        m.setMumId(key);
        Child c1 = new Child();
        c1.setName("Samuel");
        List children =  new ArrayList<Child>();
        children.add(c1);
        m.setChildren(children);
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
                        assertEquals(m,e1);
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

    @After
    public void after() throws Exception{
        while (getTestsCount() > 0) {
            Thread.sleep(1000);
        }
    }

}
