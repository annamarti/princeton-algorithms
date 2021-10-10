import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF gridWithTopAndBottom;
    private final WeightedQuickUnionUF gridWithTop;
    private boolean[] openBlocks;
    private final int gridSize;
    private final int top;
    private final int bottom;
    private int openSitesCount;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        gridSize = n;
        int gridSizeSq = gridSize * gridSize;
        openBlocks = new boolean[gridSizeSq];
        gridWithTopAndBottom = new WeightedQuickUnionUF(gridSizeSq + 2);
        gridWithTop = new WeightedQuickUnionUF(gridSizeSq + 1);
        openSitesCount = 0;
        top = n * n;
        bottom = n * n + 1;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);
        int index = getIndex(row, col);
        if (!isOpen(row, col)) {
            openIndex(index);
            connectToOpened(row, col);
        }
    }

    private void openIndex(int index) {
        openBlocks[index] = true;
        openSitesCount++;
    }

    private void connectToOpened(int row, int col) {
        int current = getIndex(row, col);


        // if cell on the top
        if (row == 1) {
            union(gridWithTopAndBottom, top, current);
            union(gridWithTop, top, current);
        }

        // if cell on the bottom
        if (row == gridSize) {
            union(gridWithTopAndBottom, current, bottom);
        }

        // connect with the cell upper
        if (row - 1 >= 1 && isOpen(row - 1, col)) {
            union(gridWithTopAndBottom, current, getIndex(row - 1, col));
            union(gridWithTop, current, getIndex(row - 1, col));
        }

        // connect with the  cell bellow
        if (row + 1 <= gridSize && isOpen(row + 1, col)) {
            union(gridWithTopAndBottom, current, getIndex(row + 1, col));
            union(gridWithTop, current, getIndex(row + 1, col));
        }

        // connect with the cell left
        if (col - 1 >= 1 && isOpen(row, col - 1)) {
            union(gridWithTopAndBottom, current, getIndex(row, col - 1));
            union(gridWithTop, current, getIndex(row, col - 1));
        }

        // connect with the cell right
        if (col + 1 <= gridSize && isOpen(row, col + 1)) {
            union(gridWithTopAndBottom, current, getIndex(row, col + 1));
            union(gridWithTop, current, getIndex(row, col + 1));
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        int index = getIndex(row, col);
        return openBlocks[index];
    }

    private void union(WeightedQuickUnionUF grid, int a, int b) {
        grid.union(a, b);
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);
        int index = getIndex(row, col);
        return gridWithTop.find(top) == gridWithTop.find(index);
    }

    private void validate(int row, int col) {
        if (row < 1 || row > gridSize || col < 1 || col > gridSize) {
            throw new IllegalArgumentException();
        }
    }

    private int getIndex(int row, int col) {
        return (row - 1) * gridSize + col - 1;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSitesCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return gridWithTopAndBottom.find(top) == gridWithTopAndBottom.find(bottom);
    }

    // // test client (optional)
    // public static void main(String[] args) {
    //
    // }
}