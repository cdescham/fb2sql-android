/*************************************************************************
 *
 *  Copyright (c) [2009] - [2019] Inventivelink
 *  All Rights Reserved.
 *
 ************************************************************************/

package inventivelink.com.fb2sql;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.annotations.PublicApi;
import com.google.firebase.database.core.utilities.encoding.CustomClassMapper;
import com.google.firebase.database.util.JsonMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SQLDataSnapshot {
    private String jsonString;
    private String table;
    private String key;
    private  Map<String, Object> cachedValue = null;
    private Iterable<SQLDataSnapshot> cachedValues = null;
    private Long childrenCount = null;


    public SQLDataSnapshot(String jsonString,String table) {
        this.jsonString = jsonString;
        this.table = table;
    }

    @PublicApi
    public @Nullable <T> T getValue(@NonNull Class<T> valueType) {
        if (cachedValue != null) {
            return CustomClassMapper.convertToCustomClass(cachedValue, valueType);
        }

        if (jsonString == null)
            return null;
        Map<String, Object> value = null;
        try {
            value = (new SQLJSONCommonNormalizer()).transform(JsonMapper.parseJson(jsonString));
            List<SQLJSONTransformer> normalizers = null;
            try {
                Method m = valueType.getMethod("getNormalizers", null);
                normalizers = (List<SQLJSONTransformer>) m.invoke(null, null);
            } catch (Exception e) {
                normalizers = null;
                SQLDatabaseLogger.info("No normalizers for " + valueType);
            }
            if (normalizers != null) {
                SQLDatabaseLogger.debug("Normalizers found for  " + valueType+" "+normalizers.size());
                for (SQLJSONTransformer normalizer : normalizers)
                    value = normalizer.transform(value);
            }
            key = value.get(table.substring(0, table.length() - 1)+"Id")+"";
            cachedValue = value;
            return CustomClassMapper.convertToCustomClass(cachedValue, valueType);
        } catch (Exception e) {
            SQLDatabaseLogger.error("Exception in getValue Exception=" + e + " json=" + value);
            e.printStackTrace();
            return null;
        }
    }

    @PublicApi
    public String getKey() {
        return key;
    }

    @PublicApi
    public Iterable<SQLDataSnapshot> getChildren() {


        if (cachedValues != null)
            return cachedValues;

        if (jsonString == null) {
            cachedValues =  new Iterable<SQLDataSnapshot>() {
                @Override
                public Iterator<SQLDataSnapshot> iterator() {
                    return new Iterator<SQLDataSnapshot>() {
                        @Override
                        public boolean hasNext() {
                            return false;
                        }

                        @Override
                        @NonNull
                        public SQLDataSnapshot next() {
                            return null;
                        }

                        @Override
                        public void remove() {
                            throw new UnsupportedOperationException("remove called on immutable collection");
                        }
                    };
                }
            };
        } else {
            JsonElement jelement = new JsonParser().parse(jsonString);
            final JsonObject jobject = jelement.getAsJsonObject();
            final JsonArray children = jobject.getAsJsonArray("hydra:member");
            childrenCount = new Long(children.size());
            cachedValues = new Iterable<SQLDataSnapshot>() {
                @Override
                public Iterator<SQLDataSnapshot> iterator() {
                    final Iterator<JsonElement> c = children.iterator();
                    return new Iterator<SQLDataSnapshot>() {
                        @Override
                        public boolean hasNext() {
                            return c.hasNext();
                        }

                        @Override
                        @NonNull
                        public SQLDataSnapshot next() {
                            return new SQLDataSnapshot(c.next().toString(), table);
                        }

                        @Override
                        public void remove() {
                            throw new UnsupportedOperationException("remove called on immutable collection");
                        }
                    };
                }
            };
        }
        return cachedValues;
    }

    @PublicApi
    public Long childrenCount() {
        if (cachedValues != null)
            return childrenCount;
        if (jsonString == null)
            return 0L;
        JsonElement jelement = new JsonParser().parse(jsonString);
        JsonObject jobject = jelement.getAsJsonObject();
        final JsonArray children = jobject.getAsJsonArray("hydra:member");
        return new Long(children.size());
    }

}

