import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class PointSET {
    private final Set<Point2D> pointSet;

    // construct an empty set of points
    public PointSET() {
        pointSet = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return pointSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        checkForNull(p);
        pointSet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        checkForNull(p);
        return pointSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        pointSet.forEach(p -> p.draw());
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        checkForNull(rect);
        List<Point2D> pointsInRec;
        pointsInRec = new ArrayList<>();
        for (Point2D point : pointSet) {
            if (rect.contains(point)) {
                pointsInRec.add(point);
            }
        }
        return pointsInRec;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        checkForNull(p);
        double nearestDist = Double.POSITIVE_INFINITY;
        Point2D nearestPoint = null;
        for (Point2D point : pointSet) {
            if (p.distanceSquaredTo(point) < nearestDist) {
                nearestDist = p.distanceSquaredTo(point);
                nearestPoint = point;
            }
        }
        return nearestPoint;
    }

    private void checkForNull(Object object) {
        if (object == null) {
            throw new IllegalArgumentException();
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        Point2D[] points = new Point2D[6];
        points[0] = new Point2D(0.1, 0.2);
        points[1] = new Point2D(0.1, 0.3);
        points[2] = new Point2D(0.3, 0.2);
        points[3] = new Point2D(0.4, 0.5);
        points[4] = new Point2D(0.3, 0.5);
        points[5] = new Point2D(0.6, 0.2);

        PointSET brute = new PointSET();
        for (Point2D p : points)
            brute.insert(p);

        RectHV rect = new RectHV(0.2, 0.1, 0.3, 0.5);
        StdOut.println(brute.range(rect));
        Point2D point = new Point2D(0.36, 0.36);
        StdOut.println(brute.nearest(point));
    }
}