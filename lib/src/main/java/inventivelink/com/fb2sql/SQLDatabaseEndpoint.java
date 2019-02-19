/*************************************************************************
 *
 *  Copyright (c) [2009] - [2019] Inventivelink
 *  All Rights Reserved.
 *
 ************************************************************************/

package inventivelink.com.fb2sql;

public class SQLDatabaseEndpoint {

    public static String uriString = null;
    public static String authUser = null;
    public static String authPass = null;
    public static String authToken = null;
    public static int connectionTimeout = 10;
    public static int readTimeout = 30;
    public static int writeTimeout = 30;
    public static int connectionPoolMaxIdleConnections = 20;
    public static int connectionPoolKeepAliveDuration = 60;

    public SQLDatabaseEndpoint() {
    }

}
