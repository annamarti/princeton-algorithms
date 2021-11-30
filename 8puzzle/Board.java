import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private final int[][] tiles;
    private final int dimension;
    private int hamming;
    private int manhattan;
    private int blankI = -1;
    private int blankJ = -1;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        dimension = tiles.length;
        this.tiles = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
        hamming = -1;
        manhattan = -1;
    }

    // string representation of this board
    public String toString() {
        StringBuilder result = new StringBuilder(tiles.length).append("\n");
        result.append(dimension + "\n");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                result.append(tiles[i][j] + " ");
            }
            result.append("\n");
        }
        return result.toString();
    }

    // board dimension n
    public int dimension() {
        return dimension;
    }

    // number of tiles out of place
    public int hamming() {
        if (hamming == -1) {
            calculateHamming();
        }
        return hamming;
    }

    private void calculateHamming() {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (tiles[i][j] != i * dimension + j + 1) {
                    hamming++;
                }
            }
        }
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        if (manhattan == -1) {
            calculateManhattan();
        }
        return manhattan;
    }

    private void calculateManhattan() {
        manhattan = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int mustbeI = tiles[i][j] / dimension;
                int mustbeJ = tiles[i][j] % dimension - 1;
                int distance = Math.abs(i - mustbeI) + Math.abs(j - mustbeJ);
                manhattan += distance;
            }
        }

        // sub blank manhatten
        findBlank();
        int mustbeI = tiles[blankI][blankJ] / dimension;
        int mustbeJ = tiles[blankI][blankJ] % dimension - 1;
        int distance = Math.abs(blankI - mustbeI) + Math.abs(blankJ - mustbeJ);
        manhattan -= distance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null) {
            return false;
        }
        if (!(y instanceof Board)) {
            return false;
        }
        Board that = (Board) y;
        if (dimension != that.dimension) {
            return false;
        }
        return Arrays.deepEquals(tiles, that.tiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> boards = new ArrayList<>();
        findBlank();

        if (blankI - 1 >= 0) {
            boards.add(getNewBoardBySwapingWithBlank(blankI - 1, blankJ));
        }

        if (blankI + 1 < dimension) {
            boards.add(getNewBoardBySwapingWithBlank(blankI + 1, blankJ));
        }

        if (blankJ - 1 >= 0) {
            boards.add(getNewBoardBySwapingWithBlank(blankI, blankJ - 1));
        }

        if (blankJ + 1 < dimension) {
            boards.add(getNewBoardBySwapingWithBlank(blankI, blankJ + 1));
        }
        return boards;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        if (blankI == -1) {
            findBlank();
        }

        int i1 = -1, j1 = -1;
        int i2 = -1, j2 = -1;
        int notZeros = 0;

        for (int i = 0; i < dimension && notZeros < 2; i++) {
            for (int j = 0; j < dimension && notZeros < 2; j++) {
                if (tiles[i][j] != 0) {
                    if (notZeros == 0) {
                        i1 = i;
                        j1 = j;
                        notZeros++;
                    }
                    else if (notZeros == 1) {
                        i2 = i;
                        j2 = j;
                        notZeros++;
                    }
                }
            }
        }

        return getNewBoardBySwaping(i1, j1, i2, j2);
    }

    private void findBlank() {
        boolean blankFound = false;
        for (int i = 0; i < dimension; i++) {
            if (!blankFound) {
                for (int j = 0; j < dimension; j++) {
                    if (tiles[i][j] == 0) {
                        blankI = i;
                        blankJ = j;
                        blankFound = true;
                        break;
                    }
                }
            }
            else {
                break;
            }
        }
    }

    // get neighbor by swaping blank with (i, j)
    private Board getNewBoardBySwapingWithBlank(int i, int j) {
        return getNewBoardBySwaping(i, j, blankI, blankJ);
    }

    private Board getNewBoardBySwaping(int i1, int j1, int i2, int j2) {
        Board newBoard = new Board(tiles);
        swap(newBoard, i1, j1, i2, j2);
        return newBoard;
    }

    private void swap(Board board, int i1, int j1, int i2, int j2) {
        int temp = board.tiles[i1][j1];
        board.tiles[i1][j1] = tiles[i2][j2];
        board.tiles[i2][j2] = temp;
    }


    // unit testing (not graded)
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                blocks[i][j] = in.readInt();
            }
        }
        Board initial = new Board(blocks);

        // Board initial2 = new Board(blocks);
        // printBoard(initial);
        // System.out.println(initial.equals(initial2));
        // System.out.println("IS goal: " + initial.isGoal());
        // System.out.println("hamming distance: " + initial.hamming());
        // initial.neighbors().forEach(b -> printBoard(b));

        Board twin = initial.twin();
        System.out.println("Initial and twin");
        printBoard(initial);
        printBoard(twin);
    }

    private static void printBoard(Board board) {
        int n = board.dimension();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.printf("%3d ", board.tiles[i][j]);
            }
            System.out.println();

        }
        System.out.println("\n----------------");
    }

}