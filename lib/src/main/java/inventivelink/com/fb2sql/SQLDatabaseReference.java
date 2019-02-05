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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLDatabaseReference {
    private String table = null;
    private String id = null;
    private String geoSearch = null;
    private String parameters = null;
    private Object pivotfield = null;


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
    public SQLDatabaseReference child(@NonNull String child) {
        if (table == null)
            table = child;
        else if (id == null)
            id = child;
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
        addParameter(pivotfield + "%5Bafter%5D", secondsSinceCocoaStarts);
        return this;
    }

    @PublicApi
    public SQLDatabaseReference timestampEndAt(Object value) {
        if (pivotfield == null) {
            SQLDatabaseLogger.error("endAt called but no column defined by orderByChildDesc prior to this call. ");
            return this;
        }
        addParameter(pivotfield + "%before%5D", value);
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


    @PublicApi
    public Task<Void> setValue(@Nullable Object object) throws Exception {
        List<SQLJSONTransformer> deNormalizers = getDenormalizerForClass(object.getClass());
        String json = new Gson().toJson(object);
        if (deNormalizers != null ) {
            Map<String, Object> bouncedUpdate = CustomClassMapper.convertToPlainJavaTypes(JsonMapper.parseJson(json));
            json = new Gson().toJson(bouncedUpdate);
        }
        final TaskCompletionSource<Void> source = new TaskCompletionSource<>();
        if (object == null)
            SQLApiPlatformStore.delete(table, id, source);
        else if (id == null)
            SQLApiPlatformStore.insert(table, json, source);
        else
            SQLApiPlatformStore.update(table, id, json, source, true);
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
            final TaskCompletionSource<Void> source = new TaskCompletionSource<>();
            SQLApiPlatformStore.update(table, id, new Gson().toJson(finalHash), source, false);
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
            String json = new Gson().toJson(bouncedUpdate);
            SQLDatabaseLogger.debug("[updateProperty] original object = "+object+" json denormalized map = "+json);
            final TaskCompletionSource<Void> source = new TaskCompletionSource<>();
            SQLApiPlatformStore.update(table, id, json, source, false);
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
        return this;
    }

    @PublicApi
    public void addGeoQueryEventListener(@NonNull final SQLGeoQueryEventListener listener) {
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
    }

}