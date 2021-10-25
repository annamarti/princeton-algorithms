/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Arrays;

public class BruteCollinearPoints {

    private final Point[] points;
    private int numberOfSegments;
    private LineSegment[] segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Constructor's argument is null");
        }
        if (containsNull(points)) {
            throw new IllegalArgumentException("Contains null points");
        }
        if (containsDuplicate(points)) {
            throw new IllegalArgumentException("Contains repeated points");
        }
        this.points = points;
        this.numberOfSegments = 0;
        this.segments = new LineSegment[points.length * points.length * points.length
                * points.length];
        findSegments();
    }

    private boolean containsNull(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                return true;
            }
        }
        return false;
    }

    private boolean containsDuplicate(Point[] points) {
        for (int i = 0; i < points.length - 1; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private void findSegments() {
        int length = points.length;
        for (int i = 0; i < length - 3; i++) {
            for (int j = i + 1; j < length - 2; j++) {
                for (int k = j + 1; k < length - 1; k++) {
                    for (int l = k + 1; l < length; l++) {
                        if (isLine(points[i], points[j], points[k], points[l])) {
                            addSegment(points[i], points[j], points[k], points[l]);
                        }
                    }
                }
            }
        }
        segments = Arrays.copyOf(this.segments, numberOfSegments);
    }

    private boolean isLine(Point a, Point b, Point c, Point d) {
            double slopeAB = a.slopeTo(b);
            double slopeAC = a.slopeTo(c);
            double slopeAD = a.slopeTo(d);
            return slopeAB == slopeAC && slopeAB == slopeAD;

        // if (b.slopeTo(a) == b.slopeTo(c) && b.slopeTo(a) == b.slopeTo(d)) return true;
        // if (c.slopeTo(a) == c.slopeTo(b) && c.slopeTo(a) == c.slopeTo(d)) return true;
        // if (d.slopeTo(a) == d.slopeTo(b) && d.slopeTo(c) == b.slopeTo(b)) return true;
    }

    private void addSegment(Point a, Point b, Point c, Point d) {
        Point[] colloiears = {a, b, c, d};
        Arrays.sort(colloiears);
        segments[numberOfSegments++] = new LineSegment(colloiears[0], colloiears[3]);
    }

    // the number of line segments
    public int numberOfSegments() {
        return numberOfSegments;
    }

    // the line segments, should include each line segment containing 4 points exactly once
    public LineSegment[] segments() {
        return segments;
    }
}