import javax.swing.*;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Canvas implements Runnable
{
    private Thread T;
    private int sleep;

    private JFrame frame;
    private CanvasPane canvas;
    private Graphics2D graphic;
    private Color backgroundColor;
    private Image canvasImage;

    private Color nodeColor;
    private Color vectorColor;

    private List<Node> nodes;
	private final NeuralGas gas;

    public Canvas(String title, NeuralGas gas, int w, int h, int sleep)
    {
        this.gas = gas;
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas = new CanvasPane();
        frame.setContentPane(canvas);
        frame.setTitle(title);
        canvas.setPreferredSize(new Dimension(w, h));
        backgroundColor = Color.BLACK;
        nodeColor =  Color.WHITE;
        vectorColor = Color.BLUE;
        frame.pack();
        this.sleep = sleep;
        this.nodes = gas.getNodes();
        setVisible(true);
        T = new Thread(this);
        T.start();
    }

    public void setVisible(boolean visible)
    {
        if(graphic == null) {
            Dimension size = canvas.getSize();
            canvasImage = canvas.createImage(size.width, size.height);
            graphic = (Graphics2D)canvasImage.getGraphics();
            graphic.setColor(backgroundColor);
            graphic.fillRect(0, 0, size.width, size.height);
            graphic.setColor(Color.black);
        }
        frame.setVisible(true);
    }

    public void run()
    {
        while(true) {
            erase();

            // Printing the nodes
            graphic.setColor(nodeColor);
            ArrayList<Node> copy = new ArrayList<Node>(nodes);
            for(Node n : copy) {
                double[] x = n.getX();
                fill(new Ellipse2D.Double(x[0] - 3, x[1] - 3, 6, 6));

                ArrayList<Node> neighbours_copy = new ArrayList<Node>(n.neighbours());
                for (Node f : neighbours_copy) {
                    double[] fx = f.getX();
                    drawLine(x[0], x[1], fx[0], fx[1]);
                }
            }

            // Printing the last random vector given by the distribution
            graphic.setColor(vectorColor);
            double[] v = gas.lastVector();
            drawLine(v[0] - 2, v[1], v[0] + 2, v[1]);
            drawLine(v[0], v[1] - 2, v[0], v[1] + 2);

            repaint();

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void draw(Shape shape)
    {
        graphic.draw(shape);
    }

    public void repaint()
    {
        canvas.repaint();
    }

    public void fill(Shape shape)
    {
        graphic.fill(shape);
    }

    public void erase()
    {
        Dimension size = canvas.getSize();
        graphic.setColor(backgroundColor);
        graphic.fillRect(0, 0, size.width, size.height);
    }

    public void drawLine(double x1, double y1, double x2, double y2)
    {
        graphic.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
    }

    public void drawLine(int x1, int y1, int x2, int y2)
    {
        graphic.drawLine(x1, y1, x2, y2);
    }

    public void setSize(int width, int height)
    {
        canvas.setPreferredSize(new Dimension(width, height));
        Image oldImage = canvasImage;
        canvasImage = canvas.createImage(width, height);
        graphic = (Graphics2D)canvasImage.getGraphics();
        graphic.drawImage(oldImage, 0, 0, null);
        frame.pack();
    }

    public Dimension getSize()
    {
        return canvas.getSize();
    }

    /************************************************************************
     * Nested class CanvasPane - the actual canvas component contained in the
     * Canvas frame. This is essentially a JPanel with added capability to
     * refresh the image drawn on it.
     */
    private class CanvasPane extends JPanel
    {
        public void paint(Graphics g)
        {
            g.drawImage(canvasImage, 0, 0, null);
        }
    }
}