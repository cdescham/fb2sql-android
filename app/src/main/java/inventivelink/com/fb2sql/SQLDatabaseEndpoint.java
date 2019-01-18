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
    public static int authRetries = 3;
    public static int connectionTimeout = 10;
    public static int readTimeout = 30;
    public static int writeTimeout = 30;


    public SQLDatabaseEndpoint(String uriString, String authUser, String authPass,int authRetries, int connectionTimeout, int readTimeout, int writeTimeout) {
        this.uriString = uriString;
        this.authUser = authUser;
        this.authPass = authPass;
        this.connectionTimeout = connectionTimeout;
        this.readTimeout = readTimeout;
        this.writeTimeout = writeTimeout;
        this.authRetries = authRetries;

    }

}
