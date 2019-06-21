/*************************************************************************
 *
 *  Copyright (c) [2009] - [2019] Inventivelink
 *  All Rights Reserved.
 *
 ************************************************************************/


package inventivelink.com.fb2sql;

import com.google.android.gms.tasks.TaskCompletionSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Callback;
import okhttp3.ConnectionPool;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;

public class SQLApiPlatformStore {


    private static MediaType mediaType = MediaType.parse("application/ld+json");

    private static Long seqNum = 0L;

    private static synchronized Long getSeqNum() {
        return seqNum++;
    }


    public static TaskCompletionSource<SQLDataSnapshot> get(String table, String id, String geoSearch, String parameters) {
        SQLDatabaseEndpoint endpoint = SQLDatabase.getInstance().getEndPoint();
        final TaskCompletionSource<SQLDataSnapshot> source = new TaskCompletionSource<>();
        String point = endpoint.uriString + "/" + table + (id != null ? "/" + id : (geoSearch != null ? "/" + geoSearch : "") + "?" + parameters);
        Request request = new Request.Builder()
                .url(point)
                .header("X-AUTH-TOKEN", endpoint.authToken)
                .get()
                .build();

        final Long seq = getSeqNum();
        SQLDatabaseLogger.debug("["+seq+"][read request] " + request);

        if (endpoint.localCacheEnabled) {
            SQLDataSnapshot cachedSnap = SQLDatabaseLocalCache.getInstance().get(request.url().toString(),endpoint.localcacheTTL);
            if (cachedSnap != null) {
                SQLDatabaseLogger.debug("["+seq+"][read snapshot from local cache] " + request);
                source.setResult(cachedSnap);
                return source;
            }
        }

        synchronized (ongoing) {
            if (ongoing.containsKey(request.url().toString())) {
                SQLDatabaseLogger.debug("[" + seq + "][read similar request already in progress - waiting for result] " + request);
                ongoing.get(request.url().toString()).add(source);
                return source;
            }

            ArrayList<TaskCompletionSource<SQLDataSnapshot>> al = new ArrayList<>();
            al.add(source);
            ongoing.put(request.url().toString(), al);
        }

        enqueueReadRequestForEndpointAndExpectedReturnCode(seq,request, endpoint, 200, id, table);
        return source;
    }

    public static void insert(String table, String json, final TaskCompletionSource<Void> source,final boolean keepCache) {
        SQLDatabaseEndpoint endpoint = SQLDatabase.getInstance().getEndPoint();
        String point = endpoint.uriString + "/" + table;
        Request request = new Request.Builder()
                .url(point)
                .header("X-AUTH-TOKEN", endpoint.authToken)
                .post(RequestBody.create(mediaType, json))
                .build();
        enqueueWriteRequestForEndpointAndExpectedReturnCode(source, request, endpoint, 201,json,keepCache);
    }

    public static void update(String table, final String id, String json, final TaskCompletionSource<Void> source,final boolean keepCache) {
        SQLDatabaseEndpoint endpoint = SQLDatabase.getInstance().getEndPoint();
        final String point = endpoint.uriString + "/" + table + "/" + id;
        Request request = new Request.Builder()
                .url(point)
                .header("X-AUTH-TOKEN", endpoint.authToken)
                .put(RequestBody.create(mediaType, json))
                .build();
        enqueueWriteRequestForEndpointAndExpectedReturnCode(source, request, endpoint, 200,json,keepCache);
    }


    public static void update(final String table, final String id, final String json, final TaskCompletionSource<Void> source, final boolean insertOn404,final boolean keepCache) {
        final SQLDatabaseEndpoint endpoint = SQLDatabase.getInstance().getEndPoint();
        final String point = endpoint.uriString + "/" + table + "/" + id;
        Request request = new Request.Builder()
                .url(point)
                .header("X-AUTH-TOKEN", endpoint.authToken)
                .put(RequestBody.create(mediaType, json))
                .build();
        final Long seq  = getSeqNum();
        SQLDatabaseLogger.debug("["+seq+"][update request] " + point + " " + json);
        getClient(endpoint).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                source.setException(e);
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                try {
                    SQLDatabaseLogger.debug("["+seq+"][update response] " + point + " code = " + response.code()+" "+response.body().string());
                    if (response.code() == 404 && insertOn404) {
                        insert(table, json, source,keepCache);
                    } else if (response.code() == 200) {
                        if (endpoint.localCacheEnabled && !keepCache) {
                            SQLDatabaseLocalCache.getInstance().clear();
                        }
                        source.setResult(null);
                    } else {
                        source.setException(new Exception("["+seq+"][update response] " + point + " : " + response.code()));
                    }
                } catch (Exception e) {
                    SQLDatabaseLogger.error("["+seq+"][get4update response] exception = " + e + ":" + point);
                    e.printStackTrace();
                    source.setException(e);
                }
            }
        });
    }


    public static void delete(String table, String id, final TaskCompletionSource<Void> source,final boolean keepCache) {
        SQLDatabaseEndpoint endpoint = SQLDatabase.getInstance().getEndPoint();
        String point = endpoint.uriString + "/" + table + "/" + id;
        Request request = new Request.Builder()
                .url(endpoint.uriString + "/" + table + "/" + id)
                .header("X-AUTH-TOKEN", endpoint.authToken)
                .delete()
                .build();
        enqueueWriteRequestForEndpointAndExpectedReturnCode(source, request, endpoint, 204,null,keepCache);
        SQLDatabaseLogger.debug("DELETE:" + point);
    }


    private static OkHttpClient okHttpClient = null;

    private static synchronized OkHttpClient getClient(final SQLDatabaseEndpoint endpoint) {
        if (okHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.authenticator(new Authenticator() {
                @Override
                public Request authenticate(Route route, Response response) throws IOException {
                    String credential = Credentials.basic(endpoint.authUser, endpoint.authPass);
                    return response.request().newBuilder().header("Authorization", credential).build();
                }
            });
            builder.connectTimeout(endpoint.connectionTimeout, TimeUnit.SECONDS);
            builder.writeTimeout(endpoint.writeTimeout, TimeUnit.SECONDS);
            builder.readTimeout(endpoint.readTimeout, TimeUnit.SECONDS);
            if (endpoint.okHttpCacheEnabled)
                builder.cache(new Cache(SQLDatabase.context.getCacheDir(), endpoint.okHttpCacheSizeMb*1024*1024));
            builder.connectionPool(new ConnectionPool(endpoint.connectionPoolMaxIdleConnections, endpoint.connectionPoolKeepAliveDuration, TimeUnit.SECONDS));
            okHttpClient = builder.build();
        }

        return okHttpClient;
    }

   static ConcurrentHashMap<String, List<TaskCompletionSource<SQLDataSnapshot>>> ongoing = new ConcurrentHashMap();


    private static synchronized  SQLDataSnapshot applyTasksResult(Request request,String responseString,String table) {
        SQLDataSnapshot snap = new SQLDataSnapshot(responseString, table);
        for (TaskCompletionSource<SQLDataSnapshot> t : ongoing.get(request.url().toString())) {
            t.setResult(snap);
        }
        synchronized (ongoing) {
            ongoing.remove(request.url().toString());
        }
        return snap;
    }


    private static synchronized  void applyEmptyTasksResult(Request request) {
        for (TaskCompletionSource<SQLDataSnapshot> t : ongoing.get(request.url().toString())) {
            t.setResult(null);
        }
        synchronized (ongoing) {
            ongoing.remove(request.url().toString());
        }
    }


    private static synchronized  void applyTasksException(Request request,Exception e) {
        for (TaskCompletionSource<SQLDataSnapshot> t : ongoing.get(request.url().toString())) {
            t.setException(e);
        }
        synchronized (ongoing) {
            ongoing.remove(request.url().toString());
        }
    }

    private static synchronized void enqueueReadRequestForEndpointAndExpectedReturnCode(final Long seq,final Request request, final SQLDatabaseEndpoint endpoint, final int successReturnCode, final String id, final String table) {


        getClient(endpoint).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                try {
                    do {
                        Thread.sleep(endpoint.retryTimeOut*1000);
                        SQLDatabaseLogger.debug("["+seq+"][read request retrying] " + request);
                    } while (!SQLDatabaseConnectivityHelper.isConnectedToNetwork(SQLDatabase.context));
                    enqueueReadRequestForEndpointAndExpectedReturnCode(seq,request, endpoint, successReturnCode, id, table);
                } catch (InterruptedException exception) {

                }
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                try {
                    String responseString = response.body().string();
                    SQLDatabaseLogger.debug("["+seq+"][read response] " + request + " code = " + response.code() + " " + responseString);
                    if (response.code() == successReturnCode) {
                        SQLDataSnapshot snap = applyTasksResult(request,responseString,table);
                        if (endpoint.localCacheEnabled) {
                            SQLDatabaseLocalCache.getInstance().put(request.url().toString(),snap);
                        }
                    } else if (response.code() == 404) {
                        applyEmptyTasksResult(request);
                    }
                    else {
                        applyTasksException(request,new Exception("read exception : " + response.code()));

                    }
                } catch (Exception e) {
                    SQLDatabaseLogger.error("["+seq+"][read response] exception = " + e);
                    e.printStackTrace();
                    applyTasksException(request,e);
                }
            }
        });
    }

    private static void enqueueWriteRequestForEndpointAndExpectedReturnCode(final TaskCompletionSource<Void> source, final Request request, final SQLDatabaseEndpoint endpoint, final int successReturnCode, String json,final boolean keepCache) {
        final Long seq = getSeqNum();
        SQLDatabaseLogger.debug("["+seq+"][write request] " + request+ ":"+json);
        getClient(endpoint).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                source.setException(e);
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                try {
                    String responseString = response.body().string();
                    SQLDatabaseLogger.debug("["+seq+"][write response] " + request + " code = " + response.code() + " " + responseString);
                    if (response.code() == successReturnCode) {
                        if (endpoint.localCacheEnabled && !keepCache) {
                            SQLDatabaseLocalCache.getInstance().clear();
                        }
                        source.setResult(null);
                    } else {
                        source.setException(new Exception("write response : " + response.code()));
                    }
                } catch (Exception e) {
                    SQLDatabaseLogger.error("["+seq+"][write response] exception = " + e);
                    e.printStackTrace();
                    source.setException(e);
                }
            }
        });
    }

}
