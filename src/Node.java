import java.util.ArrayList;
import java.util.Random;

public class Node implements Runnable
{
    private Thread T;

    private int id;

    private double[] x;

    private Noder N;

    private int age;

    private Node[] friends;

    private Random r;

    public Node(int id, double[] x, Noder N, boolean start)
    {
        this.id = id;
        this.x = x;
        this.N = N;

        r = new Random();

        age = r.nextInt(99);

        if (start) {
            start();
        }
    }

    public void start() {
        friends();

        T = new Thread(this);
        T.start();
    }

    public double getId() {
        return id;
    }

    public double[] getX() {
        return x;
    }

    private void friends() {
        Node[] newfriends = new Node[2];
        double[] dists = new double[2];
        dists[0] = Double.POSITIVE_INFINITY;
        dists[1] = Double.POSITIVE_INFINITY;
        ArrayList<Node> nodes = new ArrayList<Node>(N.getNodes());
        for (Node n : nodes) {
            if (n.getId() != id) {
                double dist = dist(n);
                if (dist < dists[0]) {
                    newfriends[1] = newfriends[0];
                    dists[1] = dists[0];
                    newfriends[0] = n;
                    dists[0] = dist;
                } else if (dist < dists[1]) {
                    newfriends[1] = n;
                    dists[1] = dist;
                }
            }
        }
        friends = newfriends;
    }

    public Node[] getFriends() {
        return friends;
    }

    public double dist(Node n) {
        double d = 0;
        for(int i = 0; i < x.length; i++) {
            d += Math.pow(n.x[i] - x[i], 2);
        }
        return d;
    }

    public void run()
    {
        while(true) {
            age++;

            for (Node f : friends) {
                double[] fx = f.getX();
                double d = dist(f);
                for(int i = 0; i < x.length; i++) {
                    x[i] += (0.001 / d) * (2000 - d) * (x[i] - fx[i]);
                    //+ r.nextDouble() - 0.5;
                }
            }

            if (age % 100 == 0) {
                friends();
            }

            try {
                Thread.sleep(0, N.sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}