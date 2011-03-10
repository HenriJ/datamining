package distributions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class DbDistribution implements Distribution {
	HashMap<Long, double[]> map = new HashMap<Long, double[]>();
	ArrayList<double[]> list = null;
	int dim = 0;
	Random r = new Random();

	public DbDistribution(String host, String database, String user, String password, int dim) throws SQLException {
        this.dim = dim;

		String url = "jdbc:mysql://"+host+"/"+database+"?user="+user;
		if(!password.isEmpty()) url+= "&password="+password;

		Connection conn = DriverManager.getConnection(url);

		Statement stmt = conn.createStatement();

        ResultSet rs;

		rs = stmt.executeQuery("SELECT ip, object, instances FROM stats WHERE object IN " +
		                        "(SELECT *  FROM " + 
		                           "(SELECT object FROM stats " +
		                           "GROUP BY object  ORDER BY COUNT(object) DESC  LIMIT " + dim + ") alias)");

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

	public int dimension() {
		return dim;
	}

	public double[] generateVector() {
		return list.get(r.nextInt(list.size()));
	}

}
