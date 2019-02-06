package inventivelink.com.fb2sql;

import java.util.Map;

public class SQLJSONIRIBuilderTransformer implements SQLJSONTransformer {
    String property;
    String id;


    public SQLJSONIRIBuilderTransformer(String property, String copyFromId) {
        this.property = property;
        this.id = copyFromId;
    }

    public Map<String, Object> transform( Map<String, Object> input) {
        input.put(property, "/api/"+property+"s"+"/"+input.get(id));
        return input;
    }

}
