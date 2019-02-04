package inventivelink.com.fb2sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLJSONHashToListTransformer implements SQLJSONTransformer {
    String property;
    String key;

    public SQLJSONHashToListTransformer(String property, String key) {
        this.property = property;
        this.key = key;
    }

    public Map<String, Object> transform( Map<String, Object> input) {
        for (String key : input.keySet()) {
            if (key.equals(property)) {
                List<Object> result = new ArrayList<>();
                HashMap<String, Object> elems = (HashMap<String, Object>) input.get(key);
                for (String k : elems.keySet()) {
                    HashMap<String, Object> theObj = (HashMap<String, Object>) elems.get(k);
                    theObj.put(key, k);
                    result.add(theObj);
                }
                input.remove(elems);
                input.put(property, result);
                break;
            }
        }
        return input;
    }

}
