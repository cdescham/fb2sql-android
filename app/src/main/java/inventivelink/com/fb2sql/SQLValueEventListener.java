/*************************************************************************
 *
 *  Copyright (c) [2009] - [2019] Inventivelink
 *  All Rights Reserved.
 *
 ************************************************************************/

package inventivelink.com.fb2sql;

import android.support.annotation.NonNull;

public interface SQLValueEventListener {
    void onDataChange(@NonNull SQLDatabaseSnapshot var1);
    void onCancelled(@NonNull SQLDatabaseException var1);
}
