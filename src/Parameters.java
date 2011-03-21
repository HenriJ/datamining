
public class Parameters {

    // Parameters
    public double e_w = 0.1;
    public double e_n = 0.01;
    public int maxAge = 50;
    public int maxNodes = 100;
    public int insertInterval = 200;
    public double alpha = 0.5;
    public double beta = 0.05;
    public int lifeexpectency = 0;
    public int poison = 5;

    Parameters() {
    }

    static Parameters life(int lifeexpectency) {
        Parameters p = new Parameters();
        p.lifeexpectency = lifeexpectency;
        return p;
    }
}
