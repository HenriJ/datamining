package distributions;

import java.util.Random;

public class RectDistribution implements Distribution {

	private Random r = new Random();
	private double x,y,w,h;
	
	public RectDistribution(double x, double y, double w, double h) {
		
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	@Override
	public int dimension() {
		
		return 2;
	}
	
	@Override
	public double[] generateVector() {
		
		double[] v = new double[2];
		v[0] = x + w*r.nextDouble();
		v[1] = y + h*r.nextDouble();
		return v;
	}

}
