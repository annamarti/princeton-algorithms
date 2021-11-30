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
        if (!(y.getClass().equals(Board.class))) {
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
            boards.add(getNeighborBySwaping(blankI - 1, blankJ));
        }

        if (blankI + 1 < dimension) {
            boards.add(getNeighborBySwaping(blankI + 1, blankJ));
        }

        if (blankJ - 1 >= 0) {
            boards.add(getNeighborBySwaping(blankI, blankJ - 1));
        }

        if (blankJ + 1 < dimension) {
            boards.add(getNeighborBySwaping(blankI, blankJ + 1));
        }
        return boards;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        // Board twi
        if (blankI == -1) {
            findBlank();
        }
        int i = blankI == 0 ? blankI + 1 : blankI - 1;
        int j = blankJ == 0 ? blankJ + 1 : blankJ - 1;
        return getNeighborBySwaping(i, j);
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
    private Board getNeighborBySwaping(int i, int j) {
        Board board = new Board(tiles);
        board.tiles[blankI][blankJ] = tiles[i][j];
        board.tiles[i][j] = 0;
        return board;
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
        Board initial2 = new Board(blocks);

        printBoard(initial);
        System.out.println(initial.equals(initial2));
        System.out.println("IS goal: " + initial.isGoal());
        System.out.println("hamming distance: " + initial.hamming());
        initial.neighbors().forEach(b -> printBoard(b));
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