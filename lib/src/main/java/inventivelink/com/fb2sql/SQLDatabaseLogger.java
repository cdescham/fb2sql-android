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

    // 0 - Debug+
    // 1 - Info+
    // 2 - Warn+
    // 3 - Error

    public static  int verbosity = 2;

    public static void debug(Object o) {
        if (verbosity  == 0)
            Log.d(SQLDATABASE_TAG,o != null ? o.toString() : null);
    }

    public static void info(Object o) {
        if (verbosity  <= 1)
            Log.i(SQLDATABASE_TAG,o != null ? o.toString() : null);
    }

    public static void warn(Object o) {
        if (verbosity  <= 2)
            Log.i(SQLDATABASE_TAG,o != null ? o.toString() : null);
    }

    public static void error(Object o) {
        if (verbosity  <= 3)
            Log.i(SQLDATABASE_TAG,o != null ? o.toString() : null);
    }

}
