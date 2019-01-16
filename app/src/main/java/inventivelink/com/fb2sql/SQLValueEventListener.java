package inventivelink.com.fb2sql;

import android.support.annotation.NonNull;

public interface SQLValueEventListener {
    void onDataChange(@NonNull SQLDataSnapshot var1);
    void onCancelled(@NonNull SQLDatabaseError var1);
}
