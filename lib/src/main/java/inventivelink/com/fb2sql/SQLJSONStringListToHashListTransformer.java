package inventivelink.com.fb2sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static inventivelink.com.fb2sql.SQLDatabaseHelper.buildUriFromId;

/*
 "eventsInvitations": [
    "-LZ0UimjFgT2uWrM4X51"
  ],

->

eventsInvitations=[
{"eventId":"-LZ-DJqICoDUN4Nwerw9"}
],

*/

public class SQLJSONStringListToHashListTransformer implements SQLJSONTransformer {
    String property;
    String key;

    public SQLJSONStringListToHashListTransformer(String property, String key) {
        this.property = property;
        this.key = key;
    }

    public Map<String, Object> transform( Map<String, Object> input) {
        List<HashMap<String, Object>> list = new ArrayList<>();
        List<String> listOfKeys = (List<String>) input.get(property);
        if (listOfKeys != null) {
            for (String s : listOfKeys) {
                HashMap<String, Object> obj = new HashMap<>();
                obj.put(key,buildUriFromId(key,s));
                list.add(obj);
            }
            input.put(property, list);
        }
        return input;
    }

}
