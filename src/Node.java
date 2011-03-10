import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Node
{
    private int id;
    private double[] w;
    
    private HashMap<Node,Integer> edges;
    double error;
    
    public Node(int id, double[] x)
    {
        this.id = id;

        w = x;
        edges = new HashMap<Node,Integer>();
        error = 0;
    }

    public double getId() {
        return id;
    }

    public double[] getX() {
        return w;
    }

    public double dist2(double[] x) {
        double d = 0;
        for(int i = 0; i < x.length; i++) {
            d += Math.pow(w[i] - x[i], 2);
        }
        return d;
    }

    public double dist2(Node n) {
        return dist2(n.w);
    }
    
    public void addEdge(Node n) {
    	edges.put(n, 0);
    	n.edges.put(this,0);
    }
    
    public void removeEdge(Node n) {
    	edges.remove(n);
    	n.edges.remove(this);
    }
    
    public void incrementEdges() {
    	for(Node n : edges.keySet()) {
    		edges.put(n,edges.get(n)+1);
    	}
    }
    
    public void removeOldEdges(int maxAge) {
    	
    	Set<Node> remove = new HashSet<Node>();
    	
    	for(Node n : edges.keySet()) {
    		int age = edges.get(n);
    		if(age > maxAge) remove.add(n);
    	}
    	
    	for(Node n : remove) {
    		removeEdge(n);
    	}
    }
    
    public Set<Node> neighbours() {
    	return edges.keySet();
    }
    
    public double error() {
    	return error;
    }
    
    public void incrementError(double value) {
    	error+= value;
    }
    
    public void decreaseError(double beta) {
    	error*= (1-beta);
    }
    
    public void move(double[] v) {
    	for(int i = 0; i < v.length; i++) {
    		w[i]+= v[i];
    	}
    }
    
    public void attract(double[] x, double factor) {
    	for(int i = 0; i < x.length; i++) {
    		w[i]+= factor*(x[i]-w[i]);
    	}
    }
    
    public Node createNode(Node n, int id, double alpha) {
    	double[] x = new double[w.length];
    	for(int i = 0; i < x.length; i++) {
    		x[i] = (w[i] + n.w[i])/2;
    	}
    	
    	Node r = new Node(id, x);
    	
    	this.removeEdge(n);
    	this.addEdge(r);
    	r.addEdge(n);
    	
    	this.error*= alpha;
    	n.error*= alpha;
    	r.error = this.error;
    	
    	return r;
    }
}