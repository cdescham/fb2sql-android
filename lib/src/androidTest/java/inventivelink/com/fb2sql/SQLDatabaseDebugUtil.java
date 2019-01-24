package inventivelink.com.fb2sql;

public class SQLDatabaseDebugUtil {
    public static void dumpStackTrace(String point) {
        try {
            throw new RuntimeException("Forced stack trace for debug invoked at : "+point);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
