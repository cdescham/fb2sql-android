/*************************************************************************
 *
 *  Copyright (c) [2009] - [2019] Inventivelink
 *  All Rights Reserved.
 *
 ************************************************************************/

package inventivelink.com.fb2sql;

import com.google.firebase.annotations.PublicApi;
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


    public SQLDatabaseReference getReference(String path) {
        return  new SQLDatabaseReference(path);
    }


    public SQLDatabaseEndpoint getEndPoint() {
        if (endPoint == null)
            throw new RuntimeException("SQL Database parameters have not been configured. Use SQLDatabase.instance.setEndPoint(uri, digestUsername, digestPassword) to do so.");
        return endPoint;
    }

    public String generateKey() {
        return PushIdGenerator.generatePushChildName(System.currentTimeMillis());
    }


    // Configuration

    @PublicApi
    public SQLDatabase setUri(String uriString) {
        if (endPoint == null)
            endPoint = new SQLDatabaseEndpoint();
        endPoint.uriString = uriString;
        return this;
    }

    @PublicApi
    public SQLDatabase setAuthUser(String authUser) {
        if (endPoint == null)
            endPoint = new SQLDatabaseEndpoint();
        endPoint.authUser = authUser;
        return this;
    }

    @PublicApi
    public SQLDatabase setAuthPass(String authPass) {
        if (endPoint == null)
            endPoint = new SQLDatabaseEndpoint();
        endPoint.authPass = authPass;
        return this;
    }

    @PublicApi
    public SQLDatabase setAuthToken(String authToken) {
        if (endPoint == null)
            endPoint = new SQLDatabaseEndpoint();
        endPoint.authToken = authToken;
        return this;
    }

    @PublicApi
    public SQLDatabase setConnectionTimeout(int connectionTimeout) {
        if (endPoint == null)
            endPoint = new SQLDatabaseEndpoint();
        endPoint.connectionTimeout = connectionTimeout;
        return this;
    }

    @PublicApi
    public SQLDatabase setReadTimeout(int readTimeout) {
        if (endPoint == null)
            endPoint = new SQLDatabaseEndpoint();
        endPoint.readTimeout = readTimeout;
        return this;
    }

    @PublicApi
    public SQLDatabase setWriteTimeout(int writeTimeout) {
        if (endPoint == null)
            endPoint = new SQLDatabaseEndpoint();
        endPoint.writeTimeout = writeTimeout;
        return this;
    }

    @PublicApi
    public SQLDatabase setLogVerbosity(int verbosity) {
        SQLDatabaseLogger.verbosity = verbosity;
        return this;
    }

}

