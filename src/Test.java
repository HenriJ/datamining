import java.sql.SQLException;

import classes.Canvas;
import classes.NeuralGas;
import classes.Parameters;

import distributions.*;

public class Test {
    public static Distribution makeRect() {
        return new RectDistribution(100, 100, 600, 600);
    }

    public static Distribution makeDisc() {
        return new DiscDistribution(200, 200, 100);
    }

    public static Distribution makeU() {
        Distribution r1 = new RectDistribution(100, 100, 600, 100);
        Distribution r2 = new RectDistribution(100, 200, 100, 200);
        Distribution r3 = new RectDistribution(100, 400, 600, 100);
        Distribution r4 = new RectDistribution(250, 250, 600, 100);

        return new MergedDistribution(r1, r2, r3, r4);
    }

    public static Distribution makeSeries() throws SQLException {
        return new SeriesDistribution("riton", "datamining", "datamining", "errr", 3);
    }

    public static Distribution makeFkz() throws SQLException {
        return new FkzDistribution("riton", "datamining", "datamining", "errr");
    }
    
    public static Distribution makeMoving() throws SQLException {
        return new MovingDistribution(200, 200, 100, 1024, 768);
    }

    public static void main(String[] args) throws SQLException {
        int W = 1024;
        int H = 768;

        //Distribution d = makeRect();

        //Distribution d = makeDisc();

        //Distribution d = makeU();

        //Distribution d = makeSeries();

        //Distribution d = makeFkz();
        
        Distribution d = makeMoving();

        int lifeexpectency = (args.length > 0) ? Integer.parseInt(args[0]) : 0;

        NeuralGas gas = new NeuralGas(d, 20, 50, Parameters.life(lifeexpectency));

        Canvas c = new Canvas("Nodes", gas, W, H, 30);
    }
}
