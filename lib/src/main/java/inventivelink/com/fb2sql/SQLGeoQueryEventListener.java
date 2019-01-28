/*************************************************************************
 *
 *  Copyright (c) [2009] - [2019] Inventivelink
 *  All Rights Reserved.
 *
 ************************************************************************/

package inventivelink.com.fb2sql;

import android.support.annotation.NonNull;

public interface SQLGeoQueryEventListener {
    void onGeoQueryReady();
    void onKeyEntered(@NonNull SQLDataSnapshot snapshot);
    void onGeoQueryError(@NonNull SQLDatabaseError exception);
}