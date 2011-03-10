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
	
	
	public DbDistribution(String host, String database, String user, String password) throws SQLException {
		
		String url = "jdbc:mysql://"+host+"/"+database+"?user="+user;
		if(!password.isEmpty()) url+= "&password="+password;
		
		Connection conn = DriverManager.getConnection(url);
		
		Statement stmt = conn.createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM objects");
		rs.next();
		dim = rs.getInt(1);
		rs.close();
		
		rs = stmt.executeQuery("SELECT ip,object,instances FROM stats");
		while(rs.next()) {
			
			long ip = rs.getLong(1);
			int object = rs.getInt(2);
			int instances = rs.getInt(3);
			
			double[] table;
			if(map.containsKey(ip)) table = map.get(ip);
			else {
				table = new double[dim];
				map.put(ip,table);
			}
			
			table[object-1] = (double)instances;
		}
		
		rs.close();
		stmt.close();
		
		list = new ArrayList<double[]>(map.values());
	}
	
	@Override
	public int dimension() {
		
		return dim;
	}

	@Override
	public double[] generateVector() {
	
		return list.get(r.nextInt(dim));
	}

}
