import distributions.*;

public class Test {
    public static void main(String[] args) {
        int W = 800;
        int H = 600;

        Distribution r1 = new RectDistribution(10, 10, 100, 100);
        Distribution r2 = new RectDistribution(200, 20, 50, 50);
        Distribution r3 = new RectDistribution(100, 200, 150, 150);

        Distribution d = new MergedDistribution(r1, r2, r3);

        NeuralGas gas = new NeuralGas(d, 900000);

        Canvas c = new Canvas("Nodes", gas, W, H, 30);
    }
}
