import javax.swing.*;

import distributions.Distribution;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

    private double zoom = 1;
    private int panX = 0;
    private int panY = 0;

    private List<double[]> showDistribution;

    public Canvas(String title, NeuralGas gas, int w, int h, int sleep)
    {
        this.gas = gas;
        frame = new JFrame();
        frame.addKeyListener(new MoveKeyListener(this));
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
        if (graphic == null) {
            Dimension size = canvas.getSize();
            canvasImage = canvas.createImage(size.width, size.height);
            graphic = (Graphics2D)canvasImage.getGraphics();
            graphic.setColor(backgroundColor);
            graphic.fillRect(0, 0, size.width, size.height);
        }
        frame.setVisible(true);
    }

    public Point markChange(double[] x) {
        return new Point((int) (x[0] * zoom + panX), (int) (x[1] * zoom + panY));
    }

    public void run()
    {
        while(true) { 
            erase();

            try {
                    // Printing the nodes
                    graphic.setColor(nodeColor);
                    ArrayList<Node> copy = new ArrayList<Node>(nodes);
                    for(Node n : copy) {
                        Point p = markChange(n.getX());
                        fill(new Ellipse2D.Double(p.x - 3, p.y - 3, 6, 6));
    
                        ArrayList<Node> neighbours_copy = new ArrayList<Node>(n.neighbours());
                        for (Node f : neighbours_copy) {
                            Point pf = markChange(f.getX());
                            drawLine(p.x, p.y, pf.x, pf.y);
                        }
                    }
    
                if (showDistribution != null) {
                    // Printing a lot of points of the distribution
                    for (double[] v : showDistribution) {
                        drawVector(v);
                    }
                } else {
                    // Printing the last random vector given by the distribution
                    drawVector(gas.lastVector());
                }

            } catch(ConcurrentModificationException e) {
                continue;
            }

            repaint();

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void drawVector(double[] v)
    {
        Point p = markChange(v);
        graphic.setColor(vectorColor);
        drawLine(p.x - 2, p.y, p.x + 2, p.y);
        drawLine(p.x, p.y - 2, p.x, p.y + 2);
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

    public void showDistribution()
    {
        if (showDistribution == null) {
            showDistribution = new ArrayList<double[]>();
            Distribution d = gas.getDistribution();
            for (int i = 0; i < 1000; i++) {
                showDistribution.add(d.generateVector());
            }
        }
    }

    public void hideDistribution()
    {
        showDistribution = null;
    }

    public double zoom(double delta) {
        zoom += delta;
        return zoom;
    }

    public int panX(int delta) {
        panX += delta;
        return panX;
    }

    public int panY(int delta) {
        panY += delta;
        return panY;
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

    private class MoveKeyListener implements KeyListener {
        private Canvas c;

        public MoveKeyListener(Canvas c) {
            this.c = c;
        }

        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
            case 107: // Key +
                c.zoom(0.1);
                break;

            case 109: // Key -
                c.zoom(-0.1);
                break;

            case 37: // Key <-
                c.panX(5);
                break;

            case 39: // Key ->
                c.panX(-5);
                break;

            case 38: // Key <-
                c.panY(5);
                break;

            case 40: // Key ->
                c.panY(-5);
                break;

            case 32: // space key
                c.showDistribution();
                break;
            }
        }

        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == 32) {
                c.hideDistribution();
            }
        }

        public void keyTyped(KeyEvent e) {

        }
    }
}