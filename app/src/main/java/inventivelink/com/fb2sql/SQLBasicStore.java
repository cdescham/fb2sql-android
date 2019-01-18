/*************************************************************************
 *
 *  Copyright (c) [2009] - [2019] Inventivelink
 *  All Rights Reserved.
 *
 ************************************************************************/

package inventivelink.com.fb2sql;

import android.os.Handler;

import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.gson.Gson;

import org.json.JSONObject;

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

public class SQLBasicStore {

    public static TaskCompletionSource<SQLDatabaseSnapshot> runQuery(String action, String table, String pk, Object object) {
        SQLDatabaseEndpoint endpoint = SQLDatabase.getInstance().getEndPoint();
        final TaskCompletionSource<SQLDatabaseSnapshot> source = new TaskCompletionSource<>();

        JSONObject parameters= new JSONObject();
        try {
            parameters.put("action", action);
            parameters.put("table", table);
            parameters.put("pk", pk);
            if (object != null) {
                Gson gson = new Gson();
                parameters.put("jsonObject", gson.toJson(object));
            }
        } catch (final Exception e) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    source.setException(e);
                }
            });
           return source;
        }

        Request request = new Request.Builder()
                .url(endpoint.uriString)
                .post(RequestBody.create(MediaType.parse("application/json"), parameters.toString()))
                .build();
        getClient(endpoint).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                source.setException(e);
            }
            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                try {
                    String responseString = response.body().string();
                    JSONObject o = new JSONObject(responseString);
                    if (o.getBoolean("success")) {
                        source.setResult(new SQLDatabaseSnapshot(o.getString("data")));
                    } else {
                        source.setException(new Exception(responseString));
                    }
                } catch (Exception e) {
                    source.setException(e);
                }
            }
        });
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


}
