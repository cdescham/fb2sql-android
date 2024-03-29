package inventivelink.com.fb2sql;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class SQLDatabaseConnectivityHelper {

        public static boolean isConnectedToNetwork(Context context) {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            boolean isConnected = false;
            if (connectivityManager != null) {
                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                isConnected = (activeNetwork != null) && (activeNetwork.isConnected());
            }

            return isConnected;
        }
}
