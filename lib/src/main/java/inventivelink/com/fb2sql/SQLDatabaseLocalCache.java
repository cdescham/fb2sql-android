package inventivelink.com.fb2sql;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class SQLDatabaseLocalCache {

    private static SQLDatabaseLocalCache instance = null;
    private ConcurrentHashMap<String,CacheObject> cache = new ConcurrentHashMap<>();

    public static synchronized SQLDatabaseLocalCache getInstance() {
        if (instance == null)
            return instance = new SQLDatabaseLocalCache();
        else
            return instance;
    }

    private class CacheObject {
        public Long storedTimeStamp;
        public SQLDataSnapshot snapshot;

        public CacheObject(SQLDataSnapshot snapshot) {
            this.storedTimeStamp = System.currentTimeMillis();
            this.snapshot = snapshot;
        }
    }

    public synchronized SQLDataSnapshot get(String url,int ttl) {
        CacheObject o = cache.get(url);
        if (o != null) {
            if (o.storedTimeStamp+ ttl*1000 <= System.currentTimeMillis()) {
                cache.remove(url);
                return null;
            } else
                return o.snapshot;
        } else
            return null;
    }


    public synchronized void put(String url,SQLDataSnapshot snapshot) {
        CacheObject o = cache.get(url);
        if (o != null) {
            o.storedTimeStamp = System.currentTimeMillis();
        } else {
            cache.put(url,new CacheObject(snapshot));
        }
    }

    public synchronized void clear() {
        cache.clear();
    }


}
