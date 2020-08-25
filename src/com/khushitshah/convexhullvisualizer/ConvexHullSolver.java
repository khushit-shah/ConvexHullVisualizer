package com.khushitshah.convexhullvisualizer;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Comparator;
import java.util.LinkedList;

public class ConvexHullSolver extends Canvas implements  Runnable{
    private final int MAX_POINTS = 200;

    private Thread thread;

    private boolean isRunning = false;
    private boolean complete = false;
    public int ticks = 0;
    final LinkedList<Point> points = new LinkedList<>();
    private Polygon hull = new Polygon();

    private int leftMostIndex;
    private int curIndex;
    private int checkingIndex;
    private int curSelectedPoint;


    ConvexHullSolver() {
    }

    public static int orientation(Point p, Point q, Point r) {
        int val = (q.y - p.y) * (r.x - q.x) -
                (q.x - p.x) * (r.y - q.y);

        if (val == 0) return 0;  // collinear
        return (val > 0) ? 1 : 2; // clock or counterclock wise
    }

    synchronized void start() {
        if (isRunning) return;
        thread = new Thread(this);
        isRunning = true;
        complete = false;

        points.sort(Comparator.comparingInt(point -> point.x));

        leftMostIndex = 0;
        curIndex = leftMostIndex;
        curSelectedPoint = 1;
        checkingIndex = 2;

        hull.addPoint(points.get(leftMostIndex).x, points.get(leftMostIndex).y);

        thread.start();

    }

    synchronized void stop() {
        if(!isRunning) return;
        try {
            isRunning = false;
            thread.join();

            hull = new Polygon();

            leftMostIndex = 0;
            curIndex = leftMostIndex;
            curSelectedPoint = 0;
            checkingIndex = 0;

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while(isRunning) {
            double ns = 1000000000. / ticks;
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1) {
                if(!complete)tick(); //updates++;
                delta--;
            }
            render();
            frames++;
            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frames = 0;
            }
        }
    }

    private void tick() {

        if (hull.contains(points.get(checkingIndex))) {
            System.out.println("Skiping Index : " + checkingIndex);
            checkingIndex++;
            tick();
            return;
        }

        if (orientation(points.get(curIndex), points.get(curSelectedPoint), points.get(checkingIndex)) == 2) {
            curSelectedPoint = checkingIndex;
        }
        checkingIndex++;

        if (checkingIndex == points.size()) {
            hull.addPoint(points.get(curSelectedPoint).x, points.get(curSelectedPoint).y);
            if (curSelectedPoint == leftMostIndex) {
                System.out.println(hull.npoints);
                complete = true;
            }
            curIndex = curSelectedPoint;
            curSelectedPoint = 0;
            checkingIndex = 0;
        }

        System.out.println(curSelectedPoint + " " + checkingIndex + " " + curIndex);
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(2);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        render(g);
        g.dispose();
        bs.show();
        bs.dispose();
    }

    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        g.setColor(Color.ORANGE);

        for (Point p : points) {
            g.fillOval(p.x, p.y, 10, 10);
        }

        g.setColor(Color.GREEN);
        g.fillOval(points.get(leftMostIndex).x - 5, points.get(leftMostIndex).y - 5, 20, 20);

        g.drawLine(points.get(curIndex).x, points.get(curIndex).y, points.get(checkingIndex).x, points.get(checkingIndex).y);

        g.setColor(Color.BLUE);
        g.fillOval(points.get(checkingIndex).x, points.get(checkingIndex).y, 10, 10);

        g.drawLine(points.get(curIndex).x, points.get(curIndex).y, points.get(curSelectedPoint).x, points.get(curSelectedPoint).y);
        g.setColor(Color.red);

        g.setColor(Color.blue);
        g.fillOval(points.get(curIndex).x, points.get(curIndex).y, 10, 10);
        g.fillOval(points.get(curSelectedPoint).x, points.get(curSelectedPoint).y, 10, 10);


        g.setColor(new Color(0, 255, 0, 100));
        for (int i = 0; i < hull.npoints; i++) {
            g.fillOval(hull.xpoints[i], hull.ypoints[i], 10, 10);
        }
        g.fillPolygon(hull.xpoints, hull.ypoints, hull.npoints);
    }

    public void paint(Graphics g) {
        render(g);
    }

    public void update(Graphics g) {
        render(g);
    }
}
