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

import java.util.Iterator;
import java.util.Map;

public class SQLDatabaseSnapshot {
    private String jsonString;
    private String key;


    public SQLDatabaseSnapshot(String jsonString,String key) {
        this.jsonString = jsonString;
        this.key = key;
    }

    @PublicApi
    public <T> T getValue(@NonNull Class<T> valueType) {
        Map<String, Object> value = null;
        try {
            value = JsonMapper.parseJson(jsonString);
        } catch ( Exception e) {
            SQLDatabaseLogger.error("Unable to parse Json:"+jsonString);
            e.printStackTrace();
            return  null;
        }
        return CustomClassMapper.convertToCustomClass(value, valueType);
    }


    @PublicApi
    public String getKey() {
        return key;
    }

    @PublicApi
    public Iterable<SQLDatabaseSnapshot> getChildren()  {
        JsonElement jelement = new JsonParser().parse(jsonString);
        JsonObject jobject = jelement.getAsJsonObject();
        final JsonArray children = jobject.getAsJsonArray("hydra:member");
        return new Iterable<SQLDatabaseSnapshot>() {
            @Override
            public Iterator<SQLDatabaseSnapshot> iterator() {
                final Iterator<JsonElement> c = children.iterator();
                return new Iterator<SQLDatabaseSnapshot>() {
                    @Override
                    public boolean hasNext() {
                        return c.hasNext();
                    }
                    @Override
                    @NonNull
                    public SQLDatabaseSnapshot next() {
                        return new SQLDatabaseSnapshot( c.next().toString(),null);
                    }
                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException("remove called on immutable collection");
                    }
                };
            }
        };
    }

}

