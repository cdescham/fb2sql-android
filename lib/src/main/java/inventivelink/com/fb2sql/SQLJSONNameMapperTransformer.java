package inventivelink.com.fb2sql;

import java.util.Map;

public class SQLJSONNameMapperTransformer implements SQLJSONTransformer {
    String property;
    String newName;

    public SQLJSONNameMapperTransformer(String property, String newName) {
        this.property = property;
        this.newName = newName;
    }

    public Map<String, Object> transform( Map<String, Object> input) {
        input.put(newName, input.remove(property));
        return input;
    }

}
