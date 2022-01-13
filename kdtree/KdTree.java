import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private Node root;
    private int size;

    private class Node {
        Point2D point;
        Node left;
        Node right;
        int depth;
        Node parent;
        RectHV rect;

        public Node(Point2D point, Node parent, int depth, RectHV rect) {
            this.point = point;
            this.parent = parent;
            this.depth = depth;
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
            size++;
            return;
        }
        insert(root, p);
    }

    private void insert(Node node, Point2D point) {
        if (node.point.equals(point)) {
            return;
        }
        RectHV leftRect = getLeftRect(node);
        RectHV rightRect = getRightRect(node);
        if (less(point, node)) {
            // go left

            if (node.left == null) {
                node.left = new Node(point, node, node.depth + 1, leftRect);
                size++;
            }
            else {
                insert(node.left, point);
            }
        }
        else {
            // go right
            if (node.right == null) {
                node.right = new Node(point, node, node.depth + 1, rightRect);
                size++;
            }
            else {
                insert(node.right, point);
            }
        }

    }

    private boolean less(Point2D point, Node node) {
        if (node.depth % 2 == 0) {
            return point.x() < node.point.x();
        }
        else {
            return point.y() < node.point.y();
        }
    }

    private RectHV getLeftRect(Node node) {
        RectHV leftRect;
        if (node.depth % 2 == 0) {
            leftRect = new RectHV(node.rect.xmin(), node.rect.ymin(), node.point.x(),
                                  node.rect.ymax());
        }
        else {
            leftRect = new RectHV(node.rect.xmin(), node.rect.ymin(), node.rect.xmax(),
                                  node.point.y());
        }

        return leftRect;
    }

    private RectHV getRightRect(Node node) {
        RectHV rightRect;
        if (node.depth % 2 == 0) {
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
        return search(root, p);
    }

    private boolean search(Node node, Point2D point) {
        if (node == null) {
            return false;
        }
        if (node.point.equals(point)) {
            return true;
        }
        if (less(point, node)) {
            return search(node.left, point);
        }
        else {
            return search(node.right, point);
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

            Color lineColor = node.depth % 2 == 0 ? Color.RED : Color.BLUE;
            StdDraw.setPenColor(lineColor);
            // StdDraw.setPenRadius(0.002);
            Point2D startPoint = getStartPoint(node);
            Point2D finishPoint = getFinishPoint(node);
            startPoint.drawTo(finishPoint);

            draw(node.left);
            draw(node.right);
        }
    }

    private Point2D getStartPoint(Node node) {
        Point2D startPoint;
        if (node.depth % 2 == 0) {
            startPoint = new Point2D(node.point.x(), node.rect.ymin());
        }
        else {
            startPoint = new Point2D(node.rect.xmin(), node.point.y());
        }
        return startPoint;
    }

    private Point2D getFinishPoint(Node node) {
        Point2D finishPoint;
        if (node.depth % 2 == 0) {
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
        List<Point2D> pointsInRect = new ArrayList<>();
        range(rect, root, pointsInRect);
        return pointsInRect;
    }

    private void range(RectHV rect, Node currentNode, List<Point2D> pointsInRec) {
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
        checkForNull(p);
        if (root == null) return null;
        return nearest(p, root, root.point);
    }

    private Point2D nearest(Point2D queryPoint, Node currentNode, Point2D closestPoint) {
        double closestDistance = queryPoint.distanceSquaredTo(closestPoint);
        if (currentNode == null
                || currentNode.rect.distanceSquaredTo(queryPoint) >= closestDistance) {
            return closestPoint;
        }

        double currentNodeDistance = queryPoint.distanceSquaredTo(currentNode.point);
        if (currentNodeDistance < closestDistance) {
            closestPoint = currentNode.point;
            closestDistance = currentNodeDistance;
        }

        if (getLeftRect(currentNode).distanceSquaredTo(queryPoint)
                < getRightRect(currentNode).distanceSquaredTo(queryPoint)) {
            closestPoint = goLeft(currentNode, queryPoint, closestPoint, closestDistance);
            closestPoint = goRight(currentNode, queryPoint, closestPoint, closestDistance);
        }
        else {
            closestPoint = goRight(currentNode, queryPoint, closestPoint, closestDistance);
            closestPoint = goLeft(currentNode, queryPoint, closestPoint, closestDistance);
        }

        return closestPoint;
    }

    private Point2D goLeft(Node currentNode, Point2D queryPoint, Point2D closestPoint, double closestDistance) {
        // double closestDistance = queryPoint.distanceSquaredTo(closestPoint);
        Point2D leftNearest = nearest(queryPoint, currentNode.left, closestPoint);
        if (leftNearest != null) {
            double leftNodeDistance = leftNearest.distanceSquaredTo(queryPoint);
            if (leftNodeDistance < closestDistance) {
                closestPoint = leftNearest;
            }
        }
        return closestPoint;
    }

    private Point2D goRight(Node currentNode, Point2D queryPoint, Point2D closestPoint,
                         double closestDistance) {
        Point2D rightNearest = nearest(queryPoint, currentNode.right, closestPoint);
        if (rightNearest != null) {
            double rightNodeDistance = rightNearest.distanceSquaredTo(queryPoint);
            if (rightNodeDistance < closestDistance) {
                closestPoint = rightNearest;
            }
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

        StdDraw.setPenRadius(0.02);
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
        Point2D nearest1 = kdtree.nearest(nearest);
        StdDraw.setPenColor(Color.MAGENTA);
        StdDraw.setPenRadius(0.025);
        nearest.draw();
        nearest1.draw();
        StdDraw.show();

        kdtree.insert(new Point2D(0.5, 0.5));
        System.out.println("Kd tree size before adding duplicate : " + kdtree.size());
        kdtree.insert(new Point2D(0.5, 0.5));
        System.out.println("Kd tree size after adding duplicate : " + kdtree.size());

    }
}