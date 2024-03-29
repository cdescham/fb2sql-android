package inventivelink.com.fb2sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static inventivelink.com.fb2sql.SQLDatabaseHelper.getIdFromIri;

public class SQLJSONCommonNormalizer implements SQLJSONTransformer {



    public SQLJSONCommonNormalizer() {
    }

    public Map<String, Object> transform( Map<String, Object> input) {
        if (input.get("id") == null) {
            input.put("id",input.get("@id"));
        }
        input.remove("@id");
        input.remove("@type");
        input.remove("@context");
        for (String key : input.keySet()) {
            Object val = input.get(key);
            if (val instanceof  String && ((String) val).startsWith("/api")) {
                input.put(key,getIdFromIri((String)val));
            }
            if (val instanceof List<?>) {
                List<Object> newList = new ArrayList();
                for (Object element : (List) val) {
                    if (element instanceof  String && ((String) element).startsWith("/api")) {
                        newList.add(getIdFromIri((String)element));
                    } else {
                        newList = (List)val;
                        break;
                    }
                }
                input.put(key,newList);
            }
        }
        return input;
    }

}