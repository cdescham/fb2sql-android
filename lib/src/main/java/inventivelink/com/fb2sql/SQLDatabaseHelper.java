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
        return Uri.parse(IRI).getLastPathSegment();
    }

    public static String buildUriFromId(String property, String id) {
        return "/api/"+property+"s"+"/"+id;
    }

}



