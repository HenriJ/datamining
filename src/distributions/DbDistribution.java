package distributions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public abstract class DbDistribution implements Distribution {
	ArrayList<double[]> list = null;
	int dim = 0;
	Random r = new Random();
	Connection conn;

	abstract protected String query();
    abstract protected void execute() throws SQLException;

	public DbDistribution(String host, String database, String user, String password) throws SQLException{
        String url = "jdbc:mysql://"+host+"/"+database+"?user="+user;
        if(!password.isEmpty()) url+= "&password="+password;

        conn = DriverManager.getConnection(url);
	}

	public int dimension() {
		return dim;
	}

	public double[] generateVector() {
	    if (list == null) {
	        try {
                execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
	    }
		return list.get(r.nextInt(list.size()));
	}

}
