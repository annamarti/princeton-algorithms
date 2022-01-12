import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.util.Set;
import java.util.TreeSet;

public class KdTree {
    private Node root;
    private int size;

    private class Node {
        Point2D point;
        Node left;
        Node right;
        int level;
        Node parent;
        RectHV rect;

        public Node(Point2D point, Node parent, int level, RectHV rect) {
            this.point = point;
            this.parent = parent;
            this.level = level;
            this.rect = rect;
        }
    }

    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        checkForNull(p);
        if (root == null) {
            root = new Node(p, null, 0, new RectHV(0, 0, 1, 1));
            return;
        }
        insert(root, p, 0);
    }

    private void insert(Node node, Point2D point, int depth) {
        RectHV leftRect = getLeftRect(node, depth);
        RectHV rightRect = getRightRect(node, depth);
        if (less(point, node, depth)) {
            // go left

            if (node.left == null) {
                node.left = new Node(point, node, depth + 1, leftRect);
            }
            else {
                insert(node.left, point, depth + 1);
            }
        }
        else {
            // do right
            if (node.right == null) {
                node.right = new Node(point, node, depth + 1, rightRect);
            }
            else {
                insert(node.right, point, depth + 1);
            }
        }

    }

    private boolean less(Point2D point, Node node, int depth) {
        if (depth % 2 == 0) {
            return point.x() < node.point.x();
        }
        else {
            return point.y() < node.point.y();
        }
    }

    private RectHV getLeftRect(Node node, int depth) {
        RectHV leftRect;
        if (depth % 2 == 0) {
            leftRect = new RectHV(node.rect.xmin(), node.rect.ymin(), node.point.x(),
                                  node.rect.ymax());
        }
        else {
            leftRect = new RectHV(node.rect.xmin(), node.rect.ymin(), node.rect.xmax(),
                                  node.point.y());
        }

        return leftRect;
    }

    private RectHV getRightRect(Node node, int depth) {
        RectHV rightRect;
        if (depth % 2 == 0) {
            rightRect = new RectHV(node.point.x(), node.rect.ymin(), node.rect.xmax(),
                                   node.rect.ymax());
        }
        else {
            rightRect = new RectHV(node.rect.xmin(), node.point.y(), node.rect.xmax(),
                                   node.rect.ymax());
        }
        return rightRect;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        checkForNull(p);
        return search(root, p, 0);
    }

    private boolean search(Node node, Point2D point, int depth) {
        if (node == null) {
            return false;
        }
        if (node.point.equals(point)) {
            return true;
        }
        if (less(point, node, depth)) {
            return search(node.left, point, depth + 1);
        }
        else {
            return search(node.right, point, depth + 1);
        }
    }

    private void checkForNull(Object object) {
        if (object == null) {
            throw new IllegalArgumentException();
        }
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenColor(Color.BLACK);
        draw(root);
        new RectHV(0, 0, 1, 1).draw();
    }

    private void draw(Node node) {
        if (node != null) {
            StdDraw.setPenColor(Color.BLACK);
            StdDraw.setPenRadius(0.01);
            node.point.draw();

            Color lineColor = node.level % 2 == 0 ? Color.RED : Color.BLUE;
            StdDraw.setPenColor(lineColor);
            StdDraw.setPenRadius(0.002);
            Point2D startPoint = getStartPoint(node);
            Point2D finishPoint = getFinishPoint(node);
            startPoint.drawTo(finishPoint);
            draw(node.left);
            draw(node.right);

            // double from
            node.point.draw();
        }
    }

    private Point2D getStartPoint(Node node) {
        Point2D startPoint;
        if (node.level % 2 == 0) {
            startPoint = new Point2D(node.point.x(), node.rect.ymin());
        }
        else {
            startPoint = new Point2D(node.rect.xmin(), node.point.y());
        }
        return startPoint;
    }

    private Point2D getFinishPoint(Node node) {
        Point2D finishPoint;
        if (node.level % 2 == 0) {
            finishPoint = new Point2D(node.point.x(), node.rect.ymax());
        }
        else {
            finishPoint = new Point2D(node.rect.xmax(), node.point.y());
        }
        return finishPoint;
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        checkForNull(rect);
        Set<Point2D> pointsInRect = new TreeSet<>();
        range(rect, root, pointsInRect);
        return pointsInRect;
    }

    private void range(RectHV rect, Node currentNode, Set<Point2D> pointsInRec) {
        if (currentNode == null) {
            return;
        }
        if (!rect.intersects(currentNode.rect)) {
            return;
        }
        if (rect.contains(currentNode.point)) {
            pointsInRec.add(currentNode.point);
        }
        range(rect, currentNode.left, pointsInRec);
        range(rect, currentNode.right, pointsInRec);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (root == null) {
            return null;
        }
        else {
            return nearest(p, root, root.point, p.distanceTo(root.point));
        }
    }

    private Point2D nearest(Point2D queryPoint, Node currentNode, Point2D closestPoint,
                            double closestDistance) {
        if (currentNode == null) {
            return null;
        }

        if (currentNode.rect.distanceTo(queryPoint) > closestDistance) {
            return null;
        }

        double currentNodeDistance = queryPoint.distanceTo(currentNode.point);
        if (currentNodeDistance < closestDistance) {
            closestDistance = currentNodeDistance;
            closestPoint = currentNode.point;
        }

        Point2D leftNearest = nearest(queryPoint, currentNode.left, closestPoint, closestDistance);
        double leftNodeDistance = leftNearest != null ? leftNearest.distanceTo(queryPoint) :
                                  Double.POSITIVE_INFINITY;
        if (leftNodeDistance < closestDistance) {
            closestDistance = leftNodeDistance;
            closestPoint = leftNearest;
        }

        Point2D rightNearest = nearest(queryPoint, currentNode.right, closestPoint,
                                       closestDistance);
        double rightNodeDistance = rightNearest != null ?
                                   rightNodeDistance = rightNearest.distanceTo(queryPoint) :
                                   Double.POSITIVE_INFINITY;
        if (rightNodeDistance < closestDistance) {
            closestDistance = rightNodeDistance;
            closestPoint = rightNearest;
        }

        return closestPoint;
    }


    // unit testing of the methods (optional)
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            brute.insert(p);
        }

        StdDraw.enableDoubleBuffering();

        RectHV rect = new RectHV(0, 0.2, 1, 1);

        StdDraw.setPenRadius(.02);
        StdDraw.setPenColor(StdDraw.GREEN);
        for (Point2D p : kdtree.range(rect))
            p.draw();

        Point2D xp = new Point2D(0.9, 0.5);
        xp.draw();
        // kdtree.nearest(xp).draw();

        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.RED);
        kdtree.draw();
        StdDraw.show();

        Point2D nearest = new Point2D(0.1, 0.3);
        StdDraw.setPenColor(Color.MAGENTA);
        StdDraw.setPenRadius(0.025);
        nearest.draw();
        Point2D nearest1 = kdtree.nearest(nearest);
        nearest1.draw();
        StdDraw.show();
        System.out.println(nearest1);
    }
}