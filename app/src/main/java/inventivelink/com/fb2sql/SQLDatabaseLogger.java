package inventivelink.com.fb2sql;

import android.util.Log;

public class SQLDatabaseLogger {

    public static String SQLDATABASE_TAG = "[SQLDATABASE]";

    public static void Logd(Object o) {
        Log.d(SQLDATABASE_TAG,o != null ? o.toString() : null);
    }

    public static void Logi(Object o) {
        Log.i(SQLDATABASE_TAG,o != null ? o.toString() : null);
    }

    public static void Logw(Object o) {
        Log.i(SQLDATABASE_TAG,o != null ? o.toString() : null);
    }

    public static void Loge(Object o) {
        Log.i(SQLDATABASE_TAG,o != null ? o.toString() : null);
    }

}
