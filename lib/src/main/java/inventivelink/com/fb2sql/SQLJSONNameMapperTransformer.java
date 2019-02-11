package inventivelink.com.fb2sql;

import java.util.Map;

public class SQLJSONNameMapperTransformer implements SQLJSONTransformer {
    String property;
    String newName;

    public SQLJSONNameMapperTransformer(String jsonName, String entityName) {
        this.property = jsonName;
        this.newName = entityName;
    }

    public Map<String, Object> transform( Map<String, Object> input) {
        Object o = input.get(property);
        input.put(newName, o);
        input.remove(property);
        return input;
    }

}
