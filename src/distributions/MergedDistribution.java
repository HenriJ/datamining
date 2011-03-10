package distributions;

import java.util.Random;
import java.util.ArrayList;

public class MergedDistribution implements Distribution {
    private Random r = new Random();
    private ArrayList<Distribution> distributions = new ArrayList<Distribution>();

    public MergedDistribution(Distribution... distributions) {
        for (Distribution d : distributions) {
            this.distributions.add(d);
        }
    }

    public int dimension() {
        return 2;
    }

    public double[] generateVector() {
        Distribution d = distributions.get(r.nextInt(distributions.size()));
        return d.generateVector();
    }
}
