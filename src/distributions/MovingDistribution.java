package distributions;

import java.util.Random;

public class MovingDistribution implements Distribution {

	private Random rand = new Random();
	private double x, y, r;
	private int W, H;
	private double vx, vy;

	public MovingDistribution(double x, double y, double r, int W, int H) {
		this.x = x;
		this.y = y;
		this.r = r;
		this.W = W;
		this.H = H;
		vx = 0.02;
		vy = 0.02;
	}

	public int dimension() {
		return 2;
	}

	public double[] generateVector() {
	    x += vx;
	    y += vy;
	    
	    if (x > W || x < 0) {
	        vx *= -1;
	    }
        if (y > H || y < 0) {
            vy *= -1;
        }

		double radius, theta;
		radius = r * Math.pow(rand.nextDouble(), 0.5);
		theta  = 360 * rand.nextDouble();
		double[] v = new double[2];
		v[0] = x + radius * Math.cos(theta);
		v[1] = y + radius * Math.sin(theta);
		return v;
	}

}
