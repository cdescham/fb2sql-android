package inventivelink.com.fb2sql;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SQLJSONCommonNormalizer implements SQLJSONTransformer {


    private String getIdFromIri(String IRI) {
        return Uri.parse((String)IRI).getLastPathSegment();
    }

    public SQLJSONCommonNormalizer() {
    }

    public Map<String, Object> transform( Map<String, Object> input) {
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