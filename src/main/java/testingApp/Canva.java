package main.java.testingApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Canva extends JPanel {

    private static final long serialVersionUID = 1L;

    private ArrayList<Point> points;
    private ArrayList<Line2D> lines;

    public Canva(){
        points = new ArrayList<>();
        lines = new ArrayList<>();

        setBackground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                points.add(new Point(e.getX(), e.getY()));
                repaint();
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.black);

        for(int i = 0; i < points.size(); i++){
            Point point = points.get(i);
            g2.drawString(String.valueOf(i),(int) point.getX(),(int) point.getY()-5);
            g2.fillOval((int) point.getX(), (int) point.getY(), 5, 5);
        }
    }

    public void drawLine(int[] points) {
        Graphics2D g2d = (Graphics2D) this.getGraphics();
        Point firstPoint;
        Point secondPoint;

        for(int i = 0;i < points.length-1; i++){
            firstPoint = this.points.get(points[i]);
            secondPoint = this.points.get(points[i + 1]);
            Line2D line = new Line2D.Double(firstPoint.getX(),firstPoint.getY(),
                    secondPoint.getX(),secondPoint.getY());
            lines.add(line);
        }

        lines.forEach(g2d::draw);
    }

    public void clearLines(){
        lines = new ArrayList<>();
        repaint();
    }

    public double[][] getEuclideanDistances() {
        double[][] distances = new double[points.size()][points.size()];
        double distance;
        double x1;
        double x2;

        double y1;
        double y2;

        for (int i = 0; i < points.size() - 1; i++) {
            distances[i][i] = 0;
            for (int j = i + 1; j < points.size(); j++) {
                x1 = points.get(i).getX();
                y1 = points.get(i).getY();

                x2 = points.get(j).getX();
                y2 = points.get(j).getY();

                distance = sqrt(pow((x2 - x1), 2) + pow((y2 - y1), 2));
                distances[i][j] = distance;
                distances[j][i] = distance;
            }
        }

        return distances;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }
}
