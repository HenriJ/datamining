import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Noder implements Runnable 
{
    private Thread T;

    public int sleep = 900000;
    public List<Node> nodes;

    private int W;
    private int H;

    private int age;

    private Random r;
    
    private int maxId = 0;

    public Noder(int W, int H) {
        this.W = W;
        this.H = H;

        r = new Random();
        nodes = new ArrayList<Node>();

        for (int id = 0; id < 3; id++) {
            addNode(false);
        }

        for (Node n : nodes) {
            n.start();
        }

        T = new Thread(this);
        T.start();
    }

    private void addNode(boolean start) {
        double[] x = { 0.2 * W + r.nextDouble() * W * 0.6, 0.2 * H + r.nextDouble() * H * 0.6};
        nodes.add(new Node(maxId++, x, this, start));
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public int getW() {
        return W;
    }

    public int getH() {
        return H;
    }

    public void run()
    {
        while(true) {
            age++;

            if (age % 100 == 0) {
                addNode(true);
                System.out.println(nodes.size());
                age = 0;
            }

            if (nodes.size() > 20) {
                System.out.println("Stop");
                break;
            }

            try {
                Thread.sleep(0, sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
