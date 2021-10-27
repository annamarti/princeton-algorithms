/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Arrays;

public class FastCollinearPoints {
    private final Point[] points;
    private int numberOfSegments;
    private LineSegment[] segments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Constructor's argument is null");
        }
        if (containsNull(points)) {
            throw new IllegalArgumentException("Points contains null");
        }
        if (containsDuplicate(points)) {
            throw new IllegalArgumentException("Points contains duplicate");
        }
        this.points = Arrays.copyOf(points, points.length);
        this.numberOfSegments = 0;
        this.segments = new LineSegment[points.length];
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
        Arrays.sort(points);
        int n = points.length;
        for (int i = 0; i < n - 3; i++) {
            Point temp = this.points[i];
            Point[] copies = getArrayWithoutOne(i);
            sortBySlopeOrder(copies, temp);
            findCollinearsForPoint(copies, temp);
        }
        segments = Arrays.copyOf(segments, numberOfSegments);
    }

    private Point[] getArrayWithoutOne(int excudeInd) {
        int n = points.length;
        Point[] copies = new Point[n - 1];
        for (int i = 0; i < excudeInd; i++) {
            copies[i] = points[i];
        }
        for (int i = excudeInd + 1; i < n; i++) {
            copies[i - 1] = points[i];
        }

        return copies;
    }

    private void sortBySlopeOrder(Point[] copies, Point p) {
        Arrays.sort(copies, p.slopeOrder());
    }

    private void findCollinearsForPoint(Point[] copies, Point point) {

        int n = copies.length;
        double[] slopes = getSlopesToPoint(copies, point);
        double slope = slopes[0];
        int count = 1;
        for (int i = 1; i < n; i++) {
            if (slopes[i] == slope) {
                count++;
            }
            else {
                if (count >= 3) {
                    addSegment(point, copies, i, count);
                }
                count = 1;
                slope = slopes[i];
            }
        }

        if (count >= 3) {
            addSegment(point, copies, n, count);
        }
    }

    private double[] getSlopesToPoint(Point[] copies, Point point) {
        double[] slopes = new double[copies.length];
        for (int i = 0; i < copies.length; i++) {
            slopes[i] = point.slopeTo(copies[i]);
        }

        return slopes;
    }

    private void addSegment(Point a, Point[] copies, int from, int count) {
        Point[] colliniears = new Point[count + 1];
        for (int i = 1; i <= count; i++) {
            colliniears[i - 1] = copies[from - i];
        }
        colliniears[count] = a;
        Arrays.sort(colliniears);
        if (segments.length == numberOfSegments) {
            resizeSegments();
        }
        if (a.compareTo(colliniears[0]) == 0) {
            segments[numberOfSegments++] = new LineSegment(colliniears[0], colliniears[count]);
        }
    }

    private void resizeSegments() {
        int n = segments.length;
        LineSegment[] temp = new LineSegment[n * 2];
        for (int i = 0; i < n; i++) {
            temp[i] = segments[i];
        }
        segments = temp;
    }

    // the number of line segments
    public int numberOfSegments() {
        return numberOfSegments;
    }

    // the line segments
    public LineSegment[] segments() {
        return Arrays.copyOf(segments, numberOfSegments);
    }

}