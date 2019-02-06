package inventivelink.com.fb2sql;

import java.util.ArrayList;
import java.util.Map;

public class SQLJSONListToPropertiesTransformer implements SQLJSONTransformer {
    ArrayList<String> propertyNames;
    String key;

    public SQLJSONListToPropertiesTransformer(ArrayList<String> propertyNames, String key) {
        this.propertyNames = propertyNames;
        this.key = key;
    }

    public Map<String, Object>  transform( Map<String, Object> input) {
        try {
            ArrayList<Object> list = (ArrayList<Object>) input.get(key);
            int i = 0;
            for (String property : propertyNames) {
                input.put(property, list.get(i++));
            }
            input.remove(key);
        } catch (Exception e) {
            SQLDatabaseLogger.error("Error in denormalizer SQLJSONListToPropertiesTransformer "+e);
            e.printStackTrace();
        }
        return input;
    }

}
