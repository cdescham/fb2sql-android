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
        for (String key : input.keySet()) {
            if (key.equals(property)) {
                HashMap<String,Object> result = new HashMap<>();
                List<HashMap<String, Object>> o = (List<HashMap<String, Object>>) input.get(key);
                for (HashMap e : o) {
                    result.put((String)e.get(key),e);
                }
                input.remove(o);
                input.put(property,result);
                break;
            }
        }
        return input;
    }

}
