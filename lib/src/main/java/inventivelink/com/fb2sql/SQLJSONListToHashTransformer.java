package inventivelink.com.fb2sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLJSONListToHashTransformer implements SQLJSONTransformer {
    String property;
    String key;

    public SQLJSONListToHashTransformer(String property, String key) {
        this.property = property;
        this.key = key;
    }

    public Map<String, Object> transform( Map<String, Object> input) {
        HashMap<String,Object> hash = new HashMap<>();
        List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) input.get(property);
        for (HashMap e : list) {
            hash.put((String)e.get(key),e);
        }
        input.put(property,hash);
        return input;
    }

}
