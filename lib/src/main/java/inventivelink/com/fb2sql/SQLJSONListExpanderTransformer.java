package inventivelink.com.fb2sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*

"property": [
    {
      "subKeyName": "123",
      "subPropertyName": "blue"
    },
     {
      "subKeyName": "456",
      "subPropertyName": "red"
    }
    ...
  ]

  to
"property": {
  "blue":[123,...]
  "received": [456,...]
}
 */


public class SQLJSONListExpanderTransformer implements SQLJSONTransformer {
    String property; // friends
    String subKeyName; // friendId
    String subPropertyName; // state

    public SQLJSONListExpanderTransformer(String property, String subKeyName, String subPropertyName) {
        this.property = property;
        this.subKeyName = subKeyName;
        this.subPropertyName = subPropertyName;

    }

    public Map<String, Object> transform( Map<String, Object> input) {
        HashMap<String,Object> hash = new HashMap<>();
        List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) input.get(property);
        for (HashMap<String,Object> e : list) {
            if (hash.get(e.get(subPropertyName)) == null) {
                hash.put((String)e.get(subPropertyName),new ArrayList<String>());
            }
            ArrayList<String> current = ((ArrayList<String>) hash.get(e.get(subPropertyName)));
            current.add((String)e.get(subKeyName));
        }
        input.put(property,hash);
        return input;
    }

}
