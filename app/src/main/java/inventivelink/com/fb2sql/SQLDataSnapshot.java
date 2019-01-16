package inventivelink.com.fb2sql;

import android.support.annotation.NonNull;

import com.google.firebase.annotations.PublicApi;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.core.utilities.encoding.CustomClassMapper;
import com.google.firebase.database.snapshot.IndexedNode;
import com.google.firebase.database.snapshot.NamedNode;
import com.google.firebase.database.util.JsonMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

public class SQLDataSnapshot {
    private String jsonString;

    public SQLDataSnapshot(String jsonString) {
        SQLDatabaseLogger.Loge(jsonString);
        this.jsonString = jsonString;
    }

    @PublicApi
    public <T> T getValue(@NonNull Class<T> valueType) {
        Map<String, Object> value = null;
        try {
            value = JsonMapper.parseJson(jsonString);
        } catch ( Exception e) {
            SQLDatabaseLogger.Loge("Unable to parse Json:"+jsonString);
            e.printStackTrace();
            return  null;
        }
        return CustomClassMapper.convertToCustomClass(value, valueType);
    }

    @PublicApi
    public Iterable<SQLDataSnapshot> getChildren()  {
        JsonElement jelement = new JsonParser().parse(jsonString);
        JsonObject jobject = jelement.getAsJsonObject();
        final JsonArray children = jobject.getAsJsonArray("hydra:member");
        return new Iterable<SQLDataSnapshot>() {
            @Override
            public Iterator<SQLDataSnapshot> iterator() {
                return new Iterator<SQLDataSnapshot>() {
                    @Override
                    public boolean hasNext() {
                        return children.iterator().hasNext();
                    }
                    @Override
                    @NonNull
                    public SQLDataSnapshot next() {
                        return new SQLDataSnapshot( children.iterator().next().toString());
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

