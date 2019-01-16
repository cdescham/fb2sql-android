package inventivelink.com.fb2sql;

import android.support.annotation.NonNull;

public class SQLDatabaseError extends Exception {

    String reason = null;

    public SQLDatabaseError(@NonNull String reason) {
        this.reason = reason;
    }

    public SQLDatabaseError(@NonNull Exception e) {
        super(e);
    }

    public Exception toException() {
        return reason == null ? this : new Exception(reason);
    }

}
