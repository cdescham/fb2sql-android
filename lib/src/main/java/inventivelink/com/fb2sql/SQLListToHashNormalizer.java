package inventivelink.com.fb2sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLListToHashNormalizer implements  SQLNormalizer{
    String property;
    String key;

    public SQLListToHashNormalizer(String property,String key) {
        this.property = property;
        this.key = key;
    }

    public Map<String, Object> normalize( Map<String, Object> input) {
        for (String key : input.keySet()) {
            if (key.equals(property)) {
                HashMap<String,Object> result = new HashMap<>();
                    List<HashMap<String, Object>> o = (List<HashMap<String, Object>>) input.get(key);
                    for (HashMap e : o) {
                        result.put((String)e.get(key),e);
                    }
                    input.remove(o);
                    input.put(property,result);
                }
        }
        return input;
    }

}
