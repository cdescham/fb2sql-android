package inventivelink.com.fb2sql;

public class TestCommon {

    public static void connectSQLDatabase() {
        SQLDatabase.getInstance().setUri("http://192.168.1.99:8000/api").setAuthToken("4UFsaVeqleSIMbvRqBfnlZ0mnE");
    }
}
