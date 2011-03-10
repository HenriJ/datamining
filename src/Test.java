import distributions.Distribution;
import distributions.RectDistribution;

public class Test {
    public static void main(String[] args) {
        int W = 800;
        int H = 600;

        Distribution distribution = new RectDistribution(200,200,300,100);
        NeuralGas gas = new NeuralGas(distribution);

        Canvas c = new Canvas("Nodes", gas, W, H, 30);
    }
}
