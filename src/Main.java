import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Main extends Canvas implements Runnable {

    private final int MAX_POINTS = 200;

    private Thread thread;
    private boolean isRunning = false;

    private List<Point> points = new LinkedList<>();
    private Polygon hull = new Polygon();

    private int leftMostIndex;
    private int curIndex;
    private int checkingIndex;
    private int curSelectedPoint;

    private Main() {
        Random r = new Random();
        for (int i = 0; i < MAX_POINTS; i++) {
            int x = r.nextInt(850) + 10;
            int y = r.nextInt(600) + 10;

            points.add(new Point(x, y));
        }

        // Let's Sort the point's;

        points.sort(Comparator.comparingInt(point -> point.x));

        leftMostIndex = 0;
        curIndex = leftMostIndex;
        curSelectedPoint = 1;
        checkingIndex = 2;

        hull.addPoint(points.get(leftMostIndex).x, points.get(leftMostIndex).y);

        new Window("ConvexHull: Gift Wrapping!", 840, 620, this);
//        this.start();
    }

    public static void main(String[] args) {
        new Main();
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
        thread.start();
    }

    private synchronized void stop() {
        if (!isRunning) return;
        try {
            thread.join();
            isRunning = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            tick();
            render();
        }
        stop();

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
                isRunning = false;
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

//        g.setColor(Color.ORANGE);
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

        g.dispose();
        bs.show();
        bs.dispose();
    }
}
