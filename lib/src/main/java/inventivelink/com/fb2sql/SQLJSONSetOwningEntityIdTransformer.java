package inventivelink.com.fb2sql;

import java.util.Map;

public class SQLJSONSetOwningEntityIdTransformer implements SQLJSONTransformer {
    String objectName;
    String objectId;


    public SQLJSONSetOwningEntityIdTransformer(String objectName, String objectId) {
        this.objectName = objectName;
        this.objectId = objectId;
    }

    public Map<String, Object> transform( Map<String, Object> input) {
       input.put(objectName,"/api/"+objectName+"/"+objectId);
        return input;
    }

}