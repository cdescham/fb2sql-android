package inventivelink.com.fb2sql;

import java.util.Map;

public class SQLJSONRemovePropertyTransformer implements SQLJSONTransformer {
    String propertyName;


    public SQLJSONRemovePropertyTransformer(String propertyName) {
        this.propertyName = propertyName;
    }

    public Map<String, Object> transform( Map<String, Object> input) {
       input.remove(propertyName);
        return input;
    }

}