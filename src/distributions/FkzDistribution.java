package distributions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class FkzDistribution extends DbDistribution{

    public FkzDistribution(String host, String database, String user, String password) throws SQLException {
        super(host, database, user, password);
    }

    protected String query() {
        return 
        "SELECT freq, bro FROM fkz";
    }

    protected void execute() throws SQLException {
        Statement stmt = conn.createStatement();

        ResultSet rs;

        rs = stmt.executeQuery(query());

        list = new ArrayList<double[]>();
        while(rs.next()) {
            int freq = rs.getInt(1);
            int bro = rs.getInt(2);

            list.add(new double[]{freq, bro});
        }

        rs.close();
        stmt.close();
    }
}
