/*************************************************************************
 *
 *  Copyright (c) [2009] - [2019] Inventivelink
 *  All Rights Reserved.
 *
 ************************************************************************/

package inventivelink.com.fb2sql;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.annotations.PublicApi;
import com.google.firebase.database.core.utilities.encoding.CustomClassMapper;
import com.google.firebase.database.util.JsonMapper;
import com.google.gson.Gson;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLDatabaseReference {
    private String table = null;
    private String id = null;
    private String geoSearch = null;
    private Double geoSearchRadius;
    private String parameters = null;
    private Object pivotfield = null;
    private boolean keepCache = false;
    private String pkName = null;


    public SQLDatabaseReference(String table) {
        this.table = table;
    }


    // Limited to simple query parameters
    private void addParameter(String key, Object value) {
        String toAdd = key + "=" + value;
        parameters = parameters != null ? parameters + "&" + toAdd : toAdd;
    }

    private String getParameters() {
        return parameters != null ? parameters : "";
    }


    @PublicApi
    public SQLDatabaseReference keepCache(boolean keepCache) {
        this.keepCache = keepCache;
        return this;
    }

    @PublicApi
    public SQLDatabaseReference setGroups(String groups) {
        addParameter("groups%5B%5D",groups);
        return this;
    }

    @PublicApi
    public SQLDatabaseReference child(String nullablePk) {
        if (table == null)
            SQLDatabaseLogger.abort("getReference(<Table>) must be called prior to calling child().");
        if (id == null) {
            id = nullablePk;
            pkName = table!= null ? (table.substring(table.length() - 1).equals("s") ? table.substring(0, table.length() - 1)+"Id" : table+"Id") : null;
        } else
            SQLDatabaseLogger.abort("Child call too many times. Maximum time is 2, child(<table>).child(<primary key>)");
        return this;
    }


    @PublicApi
    public SQLDatabaseReference child(Long nullableAutoIncrementPk) {
        if (table == null)
            SQLDatabaseLogger.abort("getReference(<Table>) must be called prior to calling child().");
        if (id == null)
            id = nullableAutoIncrementPk != null ? nullableAutoIncrementPk.toString() : null;
        else
            SQLDatabaseLogger.abort("Child call too many times. Maximum time is 2, child(<table>).child(<primary key>)");
        return this;
    }

    @PublicApi
    public SQLDatabaseReference limitToFirst(Integer limit) {
        addParameter("itemsPerPage", limit);
        return this;
    }

    @PublicApi
    public SQLDatabaseReference orderByChildAsc(Object field) {
        pivotfield = field;
        addParameter("order%5B" + field + "%5D", "asc");
        return this;
    }

    @PublicApi
    public SQLDatabaseReference orderByChildDesc(Object field) {
        pivotfield = field;
        addParameter("order%5B" + field + "%5D", "desc");
        return this;
    }


    @PublicApi
    public SQLDatabaseReference timestampStartAt(Long secondsSinceCocoaStarts) { // Cocoa Starts = 978307200L GMT: Monday, January 1, 2001 12:00:00 AM
        if (pivotfield == null) {
            SQLDatabaseLogger.error("timestampStartAt called but no column defined by orderByChildDesc prior to this call. ");
            return this;
        }
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd%20HH:mm:ss");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        addParameter(pivotfield + "%5Bafter%5D", sdf.format(new Date((secondsSinceCocoaStarts+978307200)*1000L)));
        return this;
    }

    @PublicApi
    public SQLDatabaseReference timestampEndAt(Long secondsSinceCocoaStarts) {
        if (pivotfield == null) {
            SQLDatabaseLogger.error("endAt called but no column defined by orderByChildDesc prior to this call. ");
            return this;
        }
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd%20HH:mm:ss");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        addParameter(pivotfield + "%5Bbefore%5D", sdf.format(new Date((secondsSinceCocoaStarts+978307200)*1000L)));
        return this;
    }


    @PublicApi
    public SQLDatabaseReference equalTo(Object value) {
        if (pivotfield == null) {
            SQLDatabaseLogger.error("equalTo called but no column defined by orderByChildDesc prior to this call. ");
            return this;
        }
        addParameter(pivotfield.toString(), value);
        return this;
    }


    @PublicApi
    public SQLDatabaseReference whereEquals(@NonNull String property, @NonNull String value) {
        addParameter(property, value);
        return this;
    }


    private static List<SQLJSONTransformer> getDenormalizerForClass(@NonNull Class<?> clazz) {
        try {
            List<SQLJSONTransformer> denormalizers = null;
            Method m = clazz.getMethod("getDeNormalizers", null);
            denormalizers = (List<SQLJSONTransformer>) m.invoke(null, null);
            SQLDatabaseLogger.debug("DeNormalizer found for class : " + clazz + " " + (denormalizers != null ? denormalizers.size() : null));
            return denormalizers;
        } catch (Exception e) {
            SQLDatabaseLogger.debug("No DeNormalizer found for class : " + clazz);
            return null;
        }
    }


    private static Map<String, Object>  deNormalize(Map<String, Object> hash, @NonNull List<SQLJSONTransformer> denormalizers,String property) throws Exception {
            for (SQLJSONTransformer denormalizer : denormalizers) {
                HashMap<String, Object> value = (HashMap<String, Object>) hash.get(property);
                hash.put(property,denormalizer.transform(value));
            }
            return hash;
    }

    private static Map<String, Object>  deNormalize(Map<String, Object> hash, @NonNull List<SQLJSONTransformer> denormalizers) throws Exception {
        for (SQLJSONTransformer denormalizer : denormalizers) {
            hash = denormalizer.transform(hash);
        }
        return hash;
    }

    @PublicApi
    public Task<Void> setValue(@Nullable Object object) throws Exception {
        final TaskCompletionSource<Void> source = new TaskCompletionSource<>();
        if (object != null) {
            List<SQLJSONTransformer> deNormalizers = getDenormalizerForClass(object.getClass());
            String json = new Gson().toJson(object);
            Map<String, Object> bouncedUpdate = CustomClassMapper.convertToPlainJavaTypes(JsonMapper.parseJson(json));
            if (pkName != null && bouncedUpdate.get(pkName) == null)
                 bouncedUpdate.put(pkName,id);
            if (deNormalizers != null) {
                bouncedUpdate = deNormalize(bouncedUpdate, deNormalizers);
                json = new Gson().toJson(bouncedUpdate);
            }
            if (id == null)
                SQLApiPlatformStore.insert(table, json, source,keepCache);
            else
                SQLApiPlatformStore.update(table, id, json, source,true, keepCache);
        } else {
                SQLApiPlatformStore.delete(table, id, source,keepCache);
        }
        return source.getTask();
    }

    @NonNull
    @PublicApi
    public Task<Void> updateProperties(@NonNull Map<String, Object> map) {
        try {
            HashMap<String, Object> finalHash =  new HashMap<String, Object>();
            for (String property : map.keySet()) {
                Object o = map.get(property);
                List<SQLJSONTransformer> deNormalizers = getDenormalizerForClass(o.getClass());
                HashMap<String, Object> hm = new HashMap<>();
                hm.put(property, o);
                Map<String, Object> bouncedUpdate  = CustomClassMapper.convertToPlainJavaTypes(hm);
                if (deNormalizers != null)
                    bouncedUpdate = deNormalize(bouncedUpdate,deNormalizers,property);
                finalHash.put(property,bouncedUpdate.get(property));
            }
            if (pkName != null && finalHash.get(pkName) == null)
                finalHash.put(pkName,id);
            final TaskCompletionSource<Void> source = new TaskCompletionSource<>();
            SQLApiPlatformStore.update(table, id, new Gson().toJson(finalHash), source,true, keepCache);
            return source.getTask();
        } catch (Exception e) {
            return new SQLDatabaseError(e).asVoidTask();
        }
    }

    @NonNull
    @PublicApi
    public Task<Void> updateProperty(@NonNull String property, @NonNull Object object) { // OK
        try {
            List<SQLJSONTransformer> deNormalizers = getDenormalizerForClass(object.getClass());
            Map<String, Object> map = new HashMap<>();
            map.put(property, object);
            Map<String, Object> bouncedUpdate = CustomClassMapper.convertToPlainJavaTypes(map);
            if (deNormalizers != null)
                bouncedUpdate = deNormalize(bouncedUpdate,deNormalizers,property);
            if (pkName != null && bouncedUpdate.get(pkName) == null)
                bouncedUpdate.put(pkName,id);
            String json = new Gson().toJson(bouncedUpdate);
            final TaskCompletionSource<Void> source = new TaskCompletionSource<>();
            SQLApiPlatformStore.update(table, id, json, source,true,keepCache);
            return source.getTask();
        } catch (Exception e) {
            e.printStackTrace();
            return new SQLDatabaseError(e).asVoidTask();
        }
    }


    @PublicApi
    public void addListenerForSingleValueEvent(@NonNull final SQLValueEventListener listener) {
        Task<SQLDataSnapshot> source = SQLApiPlatformStore.get(table, id, geoSearch, getParameters()).getTask();
        source.addOnCompleteListener(new OnCompleteListener<SQLDataSnapshot>() {
            @Override
            public void onComplete(final @NonNull Task<SQLDataSnapshot> task) {
                if (task.isSuccessful())
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onDataChange(task.getResult());
                        }
                    });
                else
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onCancelled(new SQLDatabaseError(task.getException()));
                        }
                    });
            }
        });
    }


    @PublicApi
    public SQLDatabaseReference queryAtLocation(@NonNull Double latitude, @NonNull Double longitude, Double distance, String unit) {
        geoSearch = "geo_search/" + latitude + "/" + longitude + "/" + distance + unit;
        geoSearchRadius = distance;
        return this;
    }

    @PublicApi
    public void addGeoQueryEventListener(@NonNull final SQLGeoQueryEventListener listener) {
        if (geoSearchRadius > 0) {
            Task<SQLDataSnapshot> source = SQLApiPlatformStore.get(table, id, geoSearch, getParameters()).getTask();
            source.addOnCompleteListener(new OnCompleteListener<SQLDataSnapshot>() {
                @Override
                public void onComplete(final @NonNull Task<SQLDataSnapshot> task) {
                    if (task.isSuccessful())
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                SQLDataSnapshot list = task.getResult();
                                for (SQLDataSnapshot s : list.getChildren()) {
                                    listener.onKeyEntered(s);
                                }
                                listener.onGeoQueryReady();
                            }
                        });
                    else
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onGeoQueryError(new SQLDatabaseError(task.getException()));
                            }
                        });
                }
            });
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    listener.onGeoQueryReady();
                }
            });
        }
    }

}