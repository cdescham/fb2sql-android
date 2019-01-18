/*************************************************************************
 *
 *  Copyright (c) [2009] - [2019] Inventivelink
 *  All Rights Reserved.
 *
 ************************************************************************/

package inventivelink.com.fb2sql;

import android.util.Log;

public class SQLDatabaseLogger {

    public static String SQLDATABASE_TAG = "[SQLDATABASE]";

    public static void debug(Object o) {
        Log.d(SQLDATABASE_TAG,o != null ? o.toString() : null);
    }

    public static void info(Object o) {
        Log.i(SQLDATABASE_TAG,o != null ? o.toString() : null);
    }

    public static void warn(Object o) {
        Log.i(SQLDATABASE_TAG,o != null ? o.toString() : null);
    }

    public static void error(Object o) {
        Log.i(SQLDATABASE_TAG,o != null ? o.toString() : null);
    }

}
