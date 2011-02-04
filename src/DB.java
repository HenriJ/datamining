import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DB {
    private static DB singleton = null;
    private Connection connect = null;

    private List<String> traces = null;

    private DB(String db, String user, String pass) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(
                        "jdbc:mysql://localhost/" + db +
                                         "?user=" + user +
                                     "&password=" + pass);
            traces = new ArrayList<String>();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static DB get() {
        if (singleton == null) {
            singleton = new DB("mom", "mom", "sqaJTp3ChHJKCvEe");
        }
        return singleton;
    }

    public int execute(String sql) throws SQLException {
        Statement stmt = connect.createStatement();
        traces.add(sql);
        return stmt.executeUpdate(sql);
    }

    public ResultSet query(String sql) throws SQLException {
        Statement stmt = connect.createStatement();
        traces.add(sql);
        return stmt.executeQuery(sql);
    }

    public Query newQuery(String sql) throws SQLException {
        Query stmt = new Query(connect.prepareStatement(sql), this);
        return stmt;
    }

    public void trace(String sql) {
        traces.add(sql);
    }

    public String toString() {
        String out = "";
        for (String trace : traces) {
            out += trace + "\n";
        }
        return out;
    }
}
