import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import distributions.Distribution;


public class NeuralGas implements Runnable{
	
	private Thread T;
    public List<Node> nodes;
    private Distribution distribution;
    private int age;
    private int currentId = 0;
    private int nanosleep;

    public NeuralGas(Distribution distribution, int nanosleep) {
        this.distribution = distribution;
        this.nanosleep = nanosleep;

        nodes = new ArrayList<Node>();

        // Initialize with 3 nodes
        for (int i = 0; i < 3; i++) {
        	nodes.add(new Node(currentId++, distribution.generateVector()));
        }

        T = new Thread(this);
        T.start();
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void run()
    {
        while(true) {
            age++;

            // Generate a random vector with the distribution
            double[] x = distribution.generateVector();

            // Search for the nearest and second nearest node
            Node s = null;							// nearest node (winner node)
            Node t = null;							// second nearest node
            double ds = Double.POSITIVE_INFINITY;	// distance from nearest node
            double dt = Double.POSITIVE_INFINITY;	// distance from second nearest node
            
            for (Node n : nodes) {
            	double d = n.dist2(x);
            	if (d < ds) {
            		t = s; 
            		dt = ds;
            		s = n;
            		ds = d;
            	} else if (d < dt) {
            		t = n;
            		dt = d;
            	}
            }
            
            // Update winner node's error variable
            s.incrementError(ds);
            
            // Attract it
            s.attract(x,0.05);	// e_w factor
            
            // Attract connected nodes
            for(Node n : s.neighbours()) {
            	n.attract(x,0.0006);	// e_n factor
            }
            
            // Increment edges from s
            s.incrementEdges();
            s.removeOldEdges(100);
            
        	// Add or reset the edge to 0 between s and t
            s.addEdge(t);
            
            // Remove nodes without edges
            Set<Node> remove = new HashSet<Node>();
            for(Node n : nodes) {
            	if(n.neighbours().isEmpty())
            		remove.add(n);
            }
        	for(Node n : remove) {
        		nodes.remove(n);
        	}
            
        	if(age % 300 == 0)
        	{
        		// Look for the node with largest error
        		Node u = null;
            	double maxError = 0;
        		for(Node n : nodes) {
        			if(n.error() > maxError) {
        				u = n;
        				maxError = n.error();
        			}
        		}
        		
        		// Look for the neighbour of u with largest error
        		Node v = null;
            	maxError = 0;
        		for(Node n : u.neighbours()) {
        			if(n.error() > maxError) {
        				v = n;
        				maxError = v.error();
        			}
        		}
        		
        		// Create a new node between u and v
        		Node r = u.createNode(v,currentId++, 0.5);
        		nodes.add(r);
        	}
        	
        	// Decrease the error of all nodes
        	for(Node n : nodes) {
        		n.decreaseError(0.0005);
        	}
        	
            try {
                Thread.sleep(0, nanosleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
