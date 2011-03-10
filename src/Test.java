import java.sql.SQLException;

import distributions.*;

public class Test {
    public static void main(String[] args) throws SQLException {
        int W = 1024;
        int H = 768;

        Distribution r1 = new RectDistribution(10, 10, 100, 100);
        Distribution r2 = new RectDistribution(200, 20, 50, 50);
        Distribution r3 = new RectDistribution(100, 200, 150, 150);
        Distribution d = new MergedDistribution(r1, r2, r3);

        DbDistribution dbd = new DbDistribution("riton", "datamining", "datamining", "errr", 2);
        
        NeuralGas gas = new NeuralGas(dbd, 16, 900000);

        Canvas c = new Canvas("Nodes", gas, W, H, 30);
    }
}
