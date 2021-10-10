import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;

    private final int trialCount;
    private final double[] trialResult;
    private final int gridSize;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        trialCount = trials;
        trialResult = new double[trialCount];
        gridSize = n;
        performTrials();
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(trialResult);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(trialResult);
    }


    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - ((CONFIDENCE_95 * stddev()) / Math.sqrt(trialCount));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + ((CONFIDENCE_95 * stddev()) / Math.sqrt(trialCount));
    }


    private void performTrials() {
        Percolation percolation;
        for (int i = 0; i < trialCount; i++) {
            percolation = new Percolation(gridSize);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(1, gridSize + 1);
                int col = StdRandom.uniform(1, gridSize + 1);
                percolation.open(row, col);
            }
            trialResult[i] = (double) percolation.numberOfOpenSites() / (gridSize * gridSize);
        }
    }

    // test client (see below)
    public static void main(String[] args) {
        int gridSize;
        int trialCount;
        if (args.length >= 2) {
            gridSize = Integer.parseInt(args[0]);
            trialCount = Integer.parseInt(args[1]);
        }
        else {
            gridSize = 12;
            trialCount = 12;
        }
        PercolationStats ps = new PercolationStats(gridSize, trialCount);

        StdOut.printf("mean                    = %.16f%n", ps.mean());
        StdOut.printf("stddev                  = %.16f%n", ps.stddev());
        StdOut.printf("95%% confidence interval = [%.16f, %.16f]%n", ps.confidenceLo(), ps.confidenceHi());

    }

}
