package inventivelink.com.fb2sql;

import android.support.annotation.NonNull;

public class SQLDatabaseException extends Exception {

    String reason = null;

    public SQLDatabaseException(@NonNull String reason) {
        this.reason = reason;
    }

    public SQLDatabaseException(@NonNull Exception e) {
        super(e);
    }

    public Exception toException() {
        return reason == null ? this : new Exception(reason);
    }

}
