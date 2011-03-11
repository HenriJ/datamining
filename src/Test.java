import java.sql.SQLException;

import distributions.*;

public class Test {
    public static Distribution makeRect() {
        return new RectDistribution(100, 100, 600, 600);
    }

    public static Distribution makeDisc() {
        return new DiscDistribution(200, 200, 100);
    }

    public static Distribution makeChibre() {
        Distribution d1 = new DiscDistribution(300, 500, 100);
        Distribution d2 = new DiscDistribution(500, 500, 100);
        Distribution d3 = new RectDistribution(325, 50, 150, 350);

        return new MergedDistribution(d1, d2, d3);
    }

    public static Distribution makeU() {
        Distribution r1 = new RectDistribution(100, 100, 600, 100);
        Distribution r2 = new RectDistribution(100, 200, 100, 200);
        Distribution r3 = new RectDistribution(100, 400, 600, 100);
        Distribution r4 = new RectDistribution(250, 250, 600, 100);

        return new MergedDistribution(r1, r2, r3, r4);
    }

    public static Distribution makeDb() throws SQLException {
        return new DbDistribution("riton", "datamining", "datamining", "errr", 3);
    }

    public static void main(String[] args) throws SQLException {
        int W = 1024;
        int H = 768;

        //Distribution d = makeRect();

        //Distribution d = makeDisc();

        //Distribution d = makeChibre();

        Distribution d = makeU();

        //Distribution d = makeDb();

        NeuralGas gas = new NeuralGas(d, 16, 500000);

        Canvas c = new Canvas("Nodes", gas, W, H, 30);
    }
}
