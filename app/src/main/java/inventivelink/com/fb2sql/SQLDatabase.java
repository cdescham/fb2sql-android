/*************************************************************************
 *
 *  Copyright (c) [2009] - [2019] Inventivelink
 *  All Rights Reserved.
 *
 ************************************************************************/

package inventivelink.com.fb2sql;

import com.google.firebase.database.core.utilities.PushIdGenerator;

public class SQLDatabase {

    private static SQLDatabase instance = null;
    private SQLDatabaseEndpoint endPoint;


    public static synchronized SQLDatabase getInstance() {
        if (instance == null)
            return instance = new SQLDatabase();
        else
            return instance;
    }

    public SQLDatabaseReference getReference() {
        return  new SQLDatabaseReference("");
    }

    public SQLDatabaseReference getReference(String path) {
        return  new SQLDatabaseReference(path);
    }

    public void setEndPoint(String uriString, String authUser, String authPass,int authRetries, int connectionTimeout, int readTimeout, int writeTimeout) {
        endPoint = new SQLDatabaseEndpoint(uriString,authUser,authPass,authRetries,connectionTimeout,readTimeout,writeTimeout);
    }

    public SQLDatabaseEndpoint getEndPoint() {
        if (endPoint == null)
            throw new RuntimeException("SQL Database parameters have not been configured. Use SQLDatabase.instance.setEndPoint(uri, digestUsername, digestPassword) to do so.");
        return endPoint;
    }

    public String generateKey() {
        return PushIdGenerator.generatePushChildName(System.currentTimeMillis());
    }


}

