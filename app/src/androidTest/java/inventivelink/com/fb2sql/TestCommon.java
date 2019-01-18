package inventivelink.com.fb2sql;

public class TestCommon {

    public static void connectSQLDatabase() {
        SQLDatabase.getInstance().setEndPoint("http://192.168.1.99:8000/api","test","test",3,10,10,30);

    }
}
