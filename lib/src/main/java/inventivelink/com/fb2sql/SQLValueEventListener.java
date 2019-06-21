/*************************************************************************
 *
 *  Copyright (c) [2009] - [2019] Inventivelink
 *  All Rights Reserved.
 *
 ************************************************************************/

package inventivelink.com.fb2sql;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface SQLValueEventListener {
    void onDataChange(@Nullable SQLDataSnapshot var1);
    void onCancelled(@NonNull SQLDatabaseError var1);
}


