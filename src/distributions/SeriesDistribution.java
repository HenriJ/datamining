package distributions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class SeriesDistribution extends DbDistribution{

    public SeriesDistribution(String host, String database, String user, String password, int dim) throws SQLException {
        super(host, database, user, password);
        this.dim = dim;
    }

    protected String query() {
        return 
        "SELECT ip, object, instances FROM stats WHERE object IN " +
        "(SELECT *  FROM " + 
        "(SELECT object FROM stats " +
        "GROUP BY object  ORDER BY COUNT(object) DESC  LIMIT " + dim + ") alias)";
    }

    protected void execute() throws SQLException {
        HashMap<Long, double[]> map = new HashMap<Long, double[]>();
        Statement stmt = conn.createStatement();

        ResultSet rs;

        rs = stmt.executeQuery(query());

        ArrayList<Integer> objects = new ArrayList<Integer>();
        while(rs.next()) {
            long ip = rs.getLong(1);
            int object = rs.getInt(2);
            int instances = rs.getInt(3);

            if (!objects.contains(object)) {
                objects.add(object);
            }
            int object_id = objects.indexOf(object);

            double[] table;
            if(map.containsKey(ip)) {
                table = map.get(ip);
            } else {
                table = new double[dim];
                map.put(ip, table);
            }

            table[object_id] = (double) instances;
        }

        rs.close();
        stmt.close();

        list = new ArrayList<double[]>(map.values());
    }
}
