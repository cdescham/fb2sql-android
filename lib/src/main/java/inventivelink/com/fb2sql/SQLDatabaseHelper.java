package inventivelink.com.fb2sql;

import android.net.Uri;

public class SQLDatabaseHelper {
    public static void dumpStackTrace(String point) {
        try {
            throw new RuntimeException("Forced stack trace for debug invoked at : "+point);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public static String getIdFromIri(String IRI) {
        Uri u = Uri.parse(IRI);
        if (u.getPathSegments().size() == 3)
            return u.getLastPathSegment();
        else
            return null;
    }

    public static String buildUriFromId(String property, String id) {
        return id != null ? "/api/"+property+"s"+"/"+id : id ;
    }

}



