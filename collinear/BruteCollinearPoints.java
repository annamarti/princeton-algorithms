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
        int n = points.length;
        this.points = Arrays.copyOf(points, n);
        this.numberOfSegments = 0;
        this.segments = new LineSegment[n];
        findSegments();
    }

    private boolean containsNull(Point[] arrayToCheck) {
        for (int i = 0; i < arrayToCheck.length; i++) {
            if (arrayToCheck[i] == null) {
                return true;
            }
        }
        return false;
    }

    private boolean containsDuplicate(Point[] arrayToCheck) {
        for (int i = 0; i < arrayToCheck.length - 1; i++) {
            for (int j = i + 1; j < arrayToCheck.length; j++) {
                if (arrayToCheck[i].compareTo(arrayToCheck[j]) == 0) {
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
                    for (int r = k + 1; r < length; r++) {
                        if (isLine(points[i], points[j], points[k], points[r])) {
                            addSegment(points[i], points[j], points[k], points[r]);
                        }
                    }
                }
            }
        }
    }

    private boolean isLine(Point a, Point b, Point c, Point d) {
            double slopeAB = a.slopeTo(b);
            double slopeAC = a.slopeTo(c);
            double slopeAD = a.slopeTo(d);
            return slopeAB == slopeAC && slopeAB == slopeAD;
    }

    private void addSegment(Point a, Point b, Point c, Point d) {
        Point[] colloiears = {a, b, c, d};
        Arrays.sort(colloiears);
        if (numberOfSegments == segments.length) {
            resizeSegments();
        }
        segments[numberOfSegments++] = new LineSegment(colloiears[0], colloiears[3]);

    }

    private void resizeSegments() {
        LineSegment[] temp = new LineSegment[segments.length * 2];
        for (int i = 0; i < segments.length; i++) {
            temp[i] = segments[i];
        }
        segments = temp;
    }

    // the number of line segments
    public int numberOfSegments() {
        return numberOfSegments;
    }

    // the line segments, should include each line segment containing 4 points exactly once
    public LineSegment[] segments() {
        return Arrays.copyOf(segments, numberOfSegments);
    }
}