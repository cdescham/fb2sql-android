/*************************************************************************
 *
 *  Copyright (c) [2009] - [2019] Inventivelink
 *  All Rights Reserved.
 *
 ************************************************************************/


package inventivelink.com.fb2sql;

import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.util.JsonMapper;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
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

    private static MediaType mediaTypeJSON = MediaType.parse("application/json");
    private static MediaType mediaTypeLD_JSON = MediaType.parse("application/ld+json");



    private static MediaType getMediaType() {
        return mediaTypeLD_JSON;
    }

    private static String deNormalize(Object object) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(object);
        if (object.getClass().isAssignableFrom(SQLEntityDeNormaliser.class)) {
            try {
                List<SQLJSONTransformer> denormalizers = null;
                Method m = object.getClass().getMethod("getDeNormalizers", null);
                denormalizers = (List<SQLJSONTransformer>) m.invoke(null, null);
                if (denormalizers != null) {
                    for (SQLJSONTransformer normalizer : denormalizers) {
                        Map<String, Object> json = JsonMapper.parseJson(jsonString);
                        jsonString = normalizer.transform(json).toString();
                    }
                }
            } catch (Exception e) {
                SQLDatabaseLogger.warn("Failed retrieving/applying deNormalizers for class, returning untouched. " + object.getClass());
                e.printStackTrace();
                return gson.toJson(object);
            }
        }
        return jsonString;
    }

    public static TaskCompletionSource<SQLDataSnapshot> get(String table, String id, String geoSearch, String parameters) {
        SQLDatabaseEndpoint endpoint = SQLDatabase.getInstance().getEndPoint();
        final TaskCompletionSource<SQLDataSnapshot> source = new TaskCompletionSource<>();
        String point = endpoint.uriString+"/"+table+ (id != null ? "/"+id : ( geoSearch != null ? "/"+geoSearch : "") +"?"+parameters);
        SQLDatabaseLogger.debug("[get request] " + point );
        Request request = new Request.Builder()
                .url(point)
                .header("X-AUTH-TOKEN", endpoint.authToken)
                .get()
                .build();
        enqueueReadRequestForEndpointAndExpectedReturnCode(source,request,endpoint,200,id);
        return source;
    }

    public static void insert(String table, Object object,final TaskCompletionSource<Void> source) {
        SQLDatabaseEndpoint endpoint = SQLDatabase.getInstance().getEndPoint();
        String json = deNormalize(object);
        String point = endpoint.uriString+"/"+table;
        SQLDatabaseLogger.debug("[insert request] " + point +" "+json);
        Request request = new Request.Builder()
                .url(point)
                .header("X-AUTH-TOKEN", endpoint.authToken)
                .post(RequestBody.create(getMediaType(),json))
                .build();
        enqueueWriteRequestForEndpointAndExpectedReturnCode(source,request,endpoint,201,null);
    }

    public static void update(String table, final String id, Object object,final TaskCompletionSource<Void> source) {
        SQLDatabaseEndpoint endpoint = SQLDatabase.getInstance().getEndPoint();
        String json = deNormalize(object);
        final String point = endpoint.uriString+"/"+table+"/"+id;
        SQLDatabaseLogger.debug("[update request] " + point +" "+json);
        Request request = new Request.Builder()
                .url(point)
                .header("X-AUTH-TOKEN", endpoint.authToken)
                .put(RequestBody.create(getMediaType(),json))
                .build();
        enqueueWriteRequestForEndpointAndExpectedReturnCode(source,request,endpoint,200,null);
    }


    public static void update(final String table, final String id, final Object object, final TaskCompletionSource<Void> source, final boolean insertOn404 ) {
        SQLDatabaseEndpoint endpoint = SQLDatabase.getInstance().getEndPoint();
        final String point = endpoint.uriString+"/"+table+"/"+id;
        String json = deNormalize(object);
        Request request = new Request.Builder()
                .url(point)
                .header("X-AUTH-TOKEN", endpoint.authToken)
                .get()
                .build();
        SQLDatabaseLogger.debug("[update or insert request] " + point +" "+json);
        getClient(endpoint).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                source.setException(e);
            }
            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                try {
                    SQLDatabaseLogger.debug("[get (if exists) response] "+point+" code = " + response.code() );
                    if (response.code() == 404 && insertOn404) {
                        insert(table,object,source);
                    } else if (response.code() == 200) {
                        update(table,id,object,source);
                    } else {
                        source.setException(new Exception("[update response] "+point+" : "+response.code()));
                    }
                } catch (Exception e) {
                    SQLDatabaseLogger.error("[update response] exception = " + e +":" +point );
                    e.printStackTrace();
                    source.setException(e);
                }
            }
        });
    }


    public static void  delete(String table, String id,final TaskCompletionSource<Void> source) {
        SQLDatabaseEndpoint endpoint = SQLDatabase.getInstance().getEndPoint();
        String point = endpoint.uriString+"/" + table + "/" + id;
        SQLDatabaseLogger.debug("[delete request] " + point );
        Request request = new Request.Builder()
                .url(endpoint.uriString+"/" + table + "/" + id )
                .header("X-AUTH-TOKEN", endpoint.authToken)
                .delete()
                .build();
        enqueueWriteRequestForEndpointAndExpectedReturnCode(source,request,endpoint,204,id);
        SQLDatabaseLogger.debug("DELETE:"+point);
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

    private static void enqueueReadRequestForEndpointAndExpectedReturnCode(final TaskCompletionSource<SQLDataSnapshot> source, final Request request, SQLDatabaseEndpoint endpoint, final int successReturnCode, final String id) {
        getClient(endpoint).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                source.setException(e);
            }
            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                try {
                    String responseString = response.body().string();
                    SQLDatabaseLogger.debug("[get response] "+request+" code = " + response.code()+" "+responseString );
                    if (response.code() == successReturnCode) {
                        source.setResult(new SQLDataSnapshot(responseString,id));
                    } else {
                        source.setException(new Exception("get response : "+response.code()));
                    }
                } catch (Exception e) {
                    SQLDatabaseLogger.error("[get response] exception = " + e );
                    e.printStackTrace();
                    source.setException(e);
                }
            }
        });
    }

    private static void enqueueWriteRequestForEndpointAndExpectedReturnCode(final TaskCompletionSource<Void> source, final Request request, SQLDatabaseEndpoint endpoint, final int successReturnCode, final String id) {
        getClient(endpoint).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                source.setException(e);
            }
            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                try {
                    String responseString = response.body().string();
                    SQLDatabaseLogger.debug("[write response] "+request+" code = " + response.code()+" "+responseString );
                    if (response.code() == successReturnCode) {
                        source.setResult(null);
                    } else {
                        source.setException(new Exception("write response : "+response.code()));
                    }
                } catch (Exception e) {
                    SQLDatabaseLogger.error("[write response] exception = " + e );
                    e.printStackTrace();
                    source.setException(e);
                }
            }
        });
    }

}
