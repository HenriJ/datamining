package tests;

import classes.*;
import distributions.*;

public class TestAttraction {

    public static Distribution makeU() {
        Distribution r1 = new RectDistribution(100, 100, 600, 100);
        Distribution r2 = new RectDistribution(100, 200, 100, 200);
        Distribution r3 = new RectDistribution(100, 400, 600, 100);
        Distribution r4 = new RectDistribution(250, 250, 600, 100);

        return new MergedDistribution(r1, r2, r3, r4);
    }

    public static void main(String[] args) {
        int W = 1024;
        int H = 768;

        Distribution d = makeU();

        double e_w = (args.length > 0) ? Integer.parseInt(args[0]) : 0.1;
        double e_n = (args.length > 1) ? Integer.parseInt(args[1]) : 0.01;

        NeuralGas gas = new NeuralGas(d, 2, 0, Parameters.attraction(e_w, e_n));
    }
}
