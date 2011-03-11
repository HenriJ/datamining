package distributions;

import java.util.Random;

public class DiscDistribution implements Distribution {

	private Random rand = new Random();
	private double x, y, r;

	public DiscDistribution(double x, double y, double r) {
		this.x = x;
		this.y = y;
		this.r = r;
	}

	public int dimension() {
		return 2;
	}

	public double[] generateVector() {
		double radius, theta;
		radius = r * Math.pow(rand.nextDouble(), 0.5);
		theta  = 360 * rand.nextDouble();
		double[] v = new double[2];
		v[0] = x + radius * Math.cos(theta);
		v[1] = y + radius * Math.sin(theta);
		return v;
	}

}
