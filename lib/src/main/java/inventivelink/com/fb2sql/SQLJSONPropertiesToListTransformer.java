package inventivelink.com.fb2sql;

import java.util.ArrayList;
import java.util.Map;

public class SQLJSONPropertiesToListTransformer implements SQLJSONTransformer {
    ArrayList<String> propertyNames;
    String key;

    public SQLJSONPropertiesToListTransformer(ArrayList<String> propertyNames, String key) {
        this.propertyNames = propertyNames;
        this.key = key;
    }

    public Map<String, Object>  transform( Map<String, Object> input) {
        ArrayList<Object> grouped = new ArrayList<>();
        for (String property : propertyNames) {
            Object o = input.get(property);
            grouped.add(o);
            input.remove(o);
        }
        input.put(key,grouped);
        return input;
    }

}
