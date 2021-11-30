import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;

public class Solver {
    private final Board initial;
    private boolean solvable;
    private boolean solved;
    private LinkedList<Board> solution;


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Initial board is null");
        }
        this.initial = initial;
        solve();
    }

    private void solve() {
        MinPQ<Node> queue = new MinPQ<>();
        MinPQ<Node> twinQueue = new MinPQ<>();
        solvable = true;

        if (initial.isGoal()) {
            solved = true;
            solution = new LinkedList<>();
            solution.add(initial);
            return;
        }
        else {
            queue.insert(new Node(initial, 0));
        }

        Board twin = initial.twin();
        if (twin.isGoal()) {
            solvable = false;
            return;
        }
        else {
            twinQueue.insert(new Node(twin, 0));
        }

        while (solvable && !solved && !queue.isEmpty()) {
            process(queue, twinQueue);
        }
    }

    private void process(MinPQ<Node> queue, MinPQ<Node> twinQueue) {
        Node searchNode = queue.delMin();
        for (Board neighbor : searchNode.board.neighbors()) {
            Node neighborNode = new Node(searchNode, neighbor, searchNode.moves() + 1);
            if (neighbor.isGoal()) {
                solved = true;
                makeSolution(neighborNode);
                return;
            }
            else if (!isInPath(searchNode, neighbor)) {
                queue.insert(neighborNode);
            }
        }

        Node twinSearchNode = twinQueue.delMin();
        for (Board twinNeighbor : twinSearchNode.board.neighbors()) {
            Node twinNeighborNode = new Node(twinSearchNode, twinNeighbor,
                                             twinSearchNode.moves() + 1);
            if (twinNeighbor.isGoal()) {
                solvable = false;
                return;
            }
            else if (!isInPath(twinSearchNode, twinNeighbor)) {
                twinQueue.insert(twinNeighborNode);
            }
        }
    }

    private boolean isInPath(Node searchNode, Board neighbor) {
        Node searchPreviousNode = searchNode.previous;
        if (searchPreviousNode != null && searchPreviousNode.board != null
                && searchPreviousNode.board.equals(neighbor)) {
            return true;
        }
        return false;
    }

    private void makeSolution(Node node) {
        solution = new LinkedList<>();
        while (node != null) {
            solution.addFirst(node.board);
            node = node.previous;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        int moves = solution == null ? -1 : solution.size() - 1;
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable

    public Iterable<Board> solution() {
        return solution;
    }

    private class Node implements Comparable<Node> {
        Node previous;
        Board board;
        int manhatten;
        int moves;

        public Node(Board board, int moves) {
            this.board = board;
            this.moves = moves;
            manhatten = board.manhattan();
        }

        public Node(Node previous, Board board, int moves) {
            this.previous = previous;
            this.board = board;
            this.moves = moves;
            manhatten = board.manhattan();
        }

        public int manhatten() {
            return manhatten + moves;
        }

        public int moves() {
            return moves;
        }

        public int compareTo(Node that) {
            return Integer.compare(this.manhatten(), that.manhatten());
        }
    }


    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
    }

}