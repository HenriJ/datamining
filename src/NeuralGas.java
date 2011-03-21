import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import distributions.Distribution;


public class NeuralGas implements Runnable {

    private Thread T;
    public List<Node> nodes;
    private Distribution distribution;
    private int age;
    private int currentId = 0;
    private int nanosleep;
    private int criterion;
    private double[] x;

    private Random r = new Random();

    private double meanError;

    // Parameters
    private Parameters p;

    public NeuralGas(Distribution distribution, int criterion, int nanosleep, Parameters p) {
        this.distribution = distribution;
        this.criterion = criterion;
        this.nanosleep = nanosleep;
        this.p = p;

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

    public double[] lastVector() {
        return x;
    }

    public Distribution getDistribution() {
        return distribution;
    }

    public List<List<Node>> connexGraphs() {
        List<List<Node>> graphs = new ArrayList<List<Node>>();

        List<Node> remainingNodes = new ArrayList<Node>(nodes);

        while (remainingNodes.size() > 0) {
            List<Node> graph = new ArrayList<Node>();
            Stack<Node> stack = new Stack<Node>();
            stack.push(remainingNodes.remove(0));

            while(stack.size() > 0) {
                Node n = stack.pop();
                graph.add(n);
                for (Node v : n.neighbours()) {
                    if (remainingNodes.contains(v)) {
                        stack.push(v);
                        remainingNodes.remove(v);
                    }
                }
            }

            graphs.add(graph);
        }

        return graphs;
    }

    public boolean isCriterionMet() {
        //System.out.println(connexGraphs().size());

        return (connexGraphs().size() == criterion);
    }

    public void run()
    {
        while(true) {
            age++;

            if (age % 1000 == 0) {
                if (isCriterionMet()) {
                    System.out.println(age);
                    break;
                }
            }

            // Generate a random vector with the distribution
            x = distribution.generateVector();

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
            s.attract(x, p.e_w);	// e_w factor
            
            // Attract connected nodes
            for(Node n : s.neighbours()) {
            	n.attract(x, p.e_n);	// e_n factor
            }
            
            // Increment edges from s
            s.incrementEdges();
            s.removeOldEdges(p.maxAge);
            
        	// Add or reset the edge to 0 between s and t
            s.addEdge(t);

            // Nodes to be removed
            Set<Node> remove = new HashSet<Node>();

            // Remove old nodes
            if (p.lifeexpectency > 0) {
                for (Node n : nodes) {
                        n.incrementAge(r.nextInt((n.error() > meanError) ? p.poison : 2));
                        if (n.age() == p.lifeexpectency) {
                            Set<Node> edges = new LinkedHashSet<Node>(n.neighbours());
                            for (Node nn : edges) {
                                n.removeEdge(nn);
                            }
                        }
                }
            }

            // Remove nodes without edges
            for (Node n : nodes) {
            	if(n.neighbours().isEmpty())
            		remove.add(n);
            }

        	for(Node n : remove) {
        		nodes.remove(n);
        	}

        	if(age % p.insertInterval == 0 && nodes.size() < p.maxNodes)
        	{
        		// Look for the node with largest error
        		Node u = null;
            	double maxError = 0;
            	meanError = 0;
        		for(Node n : nodes) {
        		    meanError += n.error();
        			if(n.error() > maxError) {
        				u = n;
        				maxError = n.error();
        			}
        		}

        		meanError /= nodes.size();

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
        		Node r = u.createNode(v,currentId++, p.alpha);
        		nodes.add(r);
        	}

        	// Decrease the error of all nodes
        	for(Node n : nodes) {
        		n.decreaseError(p.beta);
        	}

            if (nanosleep > 0) {
                try {
                    Thread.sleep(0, nanosleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
