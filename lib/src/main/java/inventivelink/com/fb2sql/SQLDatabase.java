/*************************************************************************
 *
 *  Copyright (c) [2009] - [2019] Inventivelink
 *  All Rights Reserved.
 *
 ************************************************************************/

package inventivelink.com.fb2sql;

import android.content.Context;

import com.google.firebase.annotations.PublicApi;
import com.google.firebase.database.core.utilities.PushIdGenerator;

import java.io.File;

public class SQLDatabase {

    private static SQLDatabase instance = null;
    private SQLDatabaseEndpoint endPoint;

    public static synchronized SQLDatabase getInstance() {
        if (instance == null)
            return instance = new SQLDatabase();
        else
            return instance;
    }


    public SQLDatabaseReference getReference(String tableName) {
        return  new SQLDatabaseReference(tableName);
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
    public SQLDatabase setConnectionTimeout(int connectionTimeoutSeconds) {
        if (endPoint == null)
            endPoint = new SQLDatabaseEndpoint();
        endPoint.connectionTimeout = connectionTimeoutSeconds;
        return this;
    }

    @PublicApi
    public SQLDatabase setReadTimeout(int readTimeoutSeconds) {
        if (endPoint == null)
            endPoint = new SQLDatabaseEndpoint();
        endPoint.readTimeout = readTimeoutSeconds;
        return this;
    }

    @PublicApi
    public SQLDatabase setWriteTimeout(int writeTimeoutSeconds) {
        if (endPoint == null)
            endPoint = new SQLDatabaseEndpoint();
        endPoint.writeTimeout = writeTimeoutSeconds;
        return this;
    }

    @PublicApi
    public SQLDatabase setConnectionPoolMaxIdleConnections(int maxIdleConnections) {
        if (endPoint == null)
            endPoint = new SQLDatabaseEndpoint();
        endPoint.connectionPoolMaxIdleConnections = maxIdleConnections;
        return this;
    }

    @PublicApi
    public SQLDatabase setConnectionPoolKeepAliveDuration(int keepAliveDurationInSeconds) {
        if (endPoint == null)
            endPoint = new SQLDatabaseEndpoint();
        endPoint.connectionPoolKeepAliveDuration = keepAliveDurationInSeconds;
        return this;
    }

    @PublicApi
    public SQLDatabase enableCache(Context context, int sizeMb) {
        if (endPoint == null)
            endPoint = new SQLDatabaseEndpoint();
        endPoint.cacheEnabled = true;
        endPoint.contextForCache = context;
        endPoint.cacheSizeMb = sizeMb;
        return this;
    }

    @PublicApi
    public SQLDatabase setLogVerbosity(int verbosity) {
        SQLDatabaseLogger.verbosity = verbosity;
        return this;
    }


}

