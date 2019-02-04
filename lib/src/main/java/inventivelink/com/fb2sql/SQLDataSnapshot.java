/*************************************************************************
 *
 *  Copyright (c) [2009] - [2019] Inventivelink
 *  All Rights Reserved.
 *
 ************************************************************************/

package inventivelink.com.fb2sql;

import android.support.annotation.NonNull;

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
    private String key;


    public SQLDataSnapshot(String jsonString, String key) {
        this.jsonString = jsonString;
        this.key = key;
    }

    @PublicApi
    public <T> T getValue(@NonNull Class<T> valueType) {
        try {
            Map<String, Object> value = JsonMapper.parseJson(jsonString);
            List<SQLJSONTransformer> normalizers = null;
            try {
                Method m = valueType.getMethod("getNormalizers", null);
                normalizers = (List<SQLJSONTransformer>) m.invoke(null, null);
            } catch (Exception e) {
                normalizers = null;
                SQLDatabaseLogger.info("No normalizers for class" + valueType + ":" + e);
            }
            if (normalizers != null) {
                SQLDatabaseLogger.debug("Normalizers found " + normalizers.size());
                for (SQLJSONTransformer normalizer : normalizers)
                    value = normalizer.transform(value);
            }
            return CustomClassMapper.convertToCustomClass(value, valueType);
        } catch (Exception e) {
            SQLDatabaseLogger.error("Exception in getValue Exception=" + e + " json=" + jsonString);
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
        JsonElement jelement = new JsonParser().parse(jsonString);
        JsonObject jobject = jelement.getAsJsonObject();
        final JsonArray children = jobject.getAsJsonArray("hydra:member");
        return new Iterable<SQLDataSnapshot>() {
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
                        return new SQLDataSnapshot(c.next().toString(), null);
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException("remove called on immutable collection");
                    }
                };
            }
        };
    }

    @PublicApi
    public Long childrenCount() {
        JsonElement jelement = new JsonParser().parse(jsonString);
        JsonObject jobject = jelement.getAsJsonObject();
        final JsonArray children = jobject.getAsJsonArray("hydra:member");
        return new Long(children.size());
    }

}

