/*************************************************************************
 *
 *  Copyright (c) [2009] - [2019] Inventivelink
 *  All Rights Reserved.
 *
 ************************************************************************/

package inventivelink.com.fb2sql;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

public class SQLDatabaseError extends Exception {

    String reason = null;

    public SQLDatabaseError(@NonNull String reason) {
        this.reason = reason;
        SQLDatabaseLogger.error(reason);
    }

    public SQLDatabaseError(@NonNull Exception e) {
        super(e);
        SQLDatabaseLogger.error(e.toString());
    }

    public Exception toException() {
        return reason == null ? this : new Exception(reason);
    }


    public Task<Void> asVoidTask() {
        final TaskCompletionSource<Void> source = new TaskCompletionSource<>();
        source.setException(this);
        return source.getTask();
    }

}
