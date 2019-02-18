package inventivelink.com.fb2sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 favoriteLocations=[
         {
         location=/api/locations/-LZ-DJqICoDUN4Nwerw9,
@id=/api/favorite_locations/3,
        id=3,
@type=FavoriteLocations
        }
        ],

->

favoriteLocations=[-LZ-DJqICoDUN4Nwerw9],

*/

public class SQLJSONHashListToStringListTransformer implements SQLJSONTransformer {
    String property;
    String key;

    public SQLJSONHashListToStringListTransformer(String property, String key) {
        this.property = property;
        this.key = key;
    }

    public Map<String, Object> transform( Map<String, Object> input) {
        HashMap<String,Object> hash = new HashMap<>();
        List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) input.get(property);
        List<String> listOfKeys = new ArrayList<>();
        if (list != null) {
            for (HashMap e : list) {
                listOfKeys.add(SQLJSONCommonNormalizer.getIdFromIri((String) e.get(key)));
            }
            input.put(property, listOfKeys);
        }
        return input;
    }

}
