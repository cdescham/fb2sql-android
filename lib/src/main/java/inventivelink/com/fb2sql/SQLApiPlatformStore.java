/*************************************************************************
 *
 *  Copyright (c) [2009] - [2019] Inventivelink
 *  All Rights Reserved.
 *
 ************************************************************************/


package inventivelink.com.fb2sql;

import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;

public class SQLApiPlatformStore {

    public static TaskCompletionSource<SQLDatabaseSnapshot> insert(String table, Object object) {
        SQLDatabaseEndpoint endpoint = SQLDatabase.getInstance().getEndPoint();
        final TaskCompletionSource<SQLDatabaseSnapshot> source = new TaskCompletionSource<>();
        Gson gson = new Gson();
        String point = endpoint.uriString+"/"+table;
        SQLDatabaseLogger.debug("[insert] " + point +" "+gson.toJson(object));
        Request request = new Request.Builder()
                .url(point)
                .header("X-AUTH-TOKEN", endpoint.authToken)
                .post(RequestBody.create(MediaType.parse("application/json"),gson.toJson(object)))
                .build();
        enqueueRequestForEndpointAndExpectedReturnCode(source,request,endpoint,201);
        SQLDatabaseLogger.debug("POST:"+point);

        return source;
    }

    public static TaskCompletionSource<SQLDatabaseSnapshot> update(String table, String id, Object object) {
        SQLDatabaseEndpoint endpoint = SQLDatabase.getInstance().getEndPoint();
        final TaskCompletionSource<SQLDatabaseSnapshot> source = new TaskCompletionSource<>();
        Gson gson = new Gson();
        String point = endpoint.uriString+"/"+table+"/"+id;
        SQLDatabaseLogger.debug("[update] " + point +" "+gson.toJson(object));
        Request request = new Request.Builder()
                .url(point)
                .header("X-AUTH-TOKEN", endpoint.authToken)
                .put(RequestBody.create(MediaType.parse("application/json"),gson.toJson(object)))
                .build();
        enqueueRequestForEndpointAndExpectedReturnCode(source,request,endpoint,200);
        SQLDatabaseLogger.debug("PUT:"+point);
        return source;
    }

    public static TaskCompletionSource<SQLDatabaseSnapshot> get(String table, String id, String geoSearch,String parameters) {
        SQLDatabaseEndpoint endpoint = SQLDatabase.getInstance().getEndPoint();
        final TaskCompletionSource<SQLDatabaseSnapshot> source = new TaskCompletionSource<>();
        String point = endpoint.uriString+"/"+table+ (id != null ? "/"+id : ( geoSearch != null ? "/"+geoSearch : "") +"?"+parameters);
        SQLDatabaseLogger.debug("[get] " + point );
        Request request = new Request.Builder()
                .url(point)
                .header("X-AUTH-TOKEN", endpoint.authToken)
                .get()
                .build();
        enqueueRequestForEndpointAndExpectedReturnCode(source,request,endpoint,200);
        SQLDatabaseLogger.debug("GET:"+point);
        return source;
    }

    public static TaskCompletionSource<SQLDatabaseSnapshot> delete(String table, String id) {
        SQLDatabaseEndpoint endpoint = SQLDatabase.getInstance().getEndPoint();
        final TaskCompletionSource<SQLDatabaseSnapshot> source = new TaskCompletionSource<>();
        String point = endpoint.uriString+"/" + table + "/" + id;
        SQLDatabaseLogger.debug("[delete] " + point );
        Request request = new Request.Builder()
                .url(endpoint.uriString+"/" + table + "/" + id )
                .header("X-AUTH-TOKEN", endpoint.authToken)
                .delete()
                .build();
        enqueueRequestForEndpointAndExpectedReturnCode(source,request,endpoint,204);
        SQLDatabaseLogger.debug("DELETE:"+point);
        return source;
    }


    private static OkHttpClient getClient(final SQLDatabaseEndpoint endpoint) {
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

        return builder.build();
    }

    private static void enqueueRequestForEndpointAndExpectedReturnCode(final TaskCompletionSource<SQLDatabaseSnapshot> source, Request request, SQLDatabaseEndpoint endpoint,final int successReturnCode) {
        getClient(endpoint).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                source.setException(e);
            }
            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                try {
                    String responseString = response.body().string();
                    SQLDatabaseLogger.debug("[response] code = " + response.code()+" "+responseString );
                    if (response.code() == successReturnCode) {
                        source.setResult(new SQLDatabaseSnapshot(responseString));
                    } else {
                        source.setException(new Exception("response : "+response.code()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    source.setException(e);
                }
            }
        });
    }

}
