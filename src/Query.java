import java.sql.SQLException;
import java.sql.PreparedStatement;


public class Query {
    private DB db;
    private PreparedStatement pS;

    public Query(PreparedStatement pS, DB db) {
        this.pS = pS;
        this.db = db;
    }

    public int execute() throws SQLException {
        db.trace(pS.toString());
        return pS.executeUpdate();
    }

    public void setInt(int i, int j) throws SQLException {
        pS.setInt(i, j);
    }

    public void setString(int i, String path) throws SQLException {
        pS.setString(i, path);
    }

    public void setLong(int i, long size) throws SQLException {
        pS.setLong(i, size);
    }

    public void clearParameters() throws SQLException {
        pS.clearParameters();
    }
}
