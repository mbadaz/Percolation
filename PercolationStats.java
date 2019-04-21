/**
 * Name: PecolationStats Date: 20/04/2019
 *
 * @author : mbadaz (Tapiwa Muzira)
 * <p>
 * Description: {@link PercolationStats} runs Monte Carlo simulation experiments on the {@link
 * Percolation} data type and calculates the percolation threshold of each experiments. It then
 * prints the mean, standard deviation and the 95% confidence interval lower and upper bounds
 * <p>
 * The percolation threshold value is the probability p* such that when p < p* a random n-by-n grid
 * almost never percolates, and when p > p*, a random n-by-n grid almost always percolates, provided
 * that n is sufficiently large. ***************************************************************************
 */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    public static final double CONFIDENCE_INTERVAL_95 = 1.96;
    private double[] experiments;
    // the results (percolation threshold) of each experiment
    private final int t;
    private double mean;
    private double stddev;

    /**
     * Runs T number of experiment on an N*N grid and collects the results (percolation thresholds)
     * for all the experiments     *
     *
     * @param n number of row/columns of the grid
     * @param t the number of trials/experiments
     */
    public PercolationStats(int n, int t) {
        if (n <= 0 || t <= 0) throw new IllegalArgumentException();
        experiments = new double[t];
        this.t = t;
        runExperiments(this.t, n);
    }

    private void runExperiments(int trials, int n) {
        for (int i = 0; i < trials; i++) {
            // the Percolation data model
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int x = StdRandom.uniform(1, n + 1);
                int y = StdRandom.uniform(1, n + 1);
                perc.open(x, y);
            }
            experiments[i] = (perc.numberOfOpenSites() * 1.0) / (n * n * 1.0);
        }
    }

    public double mean() {
        mean = StdStats.mean(experiments);
        return mean;
    }                          // sample mean of percolation threshold

    public double stddev() {
        stddev = StdStats.stddev(experiments);
        return stddev;
    }                        // sample standard deviation of percolation threshold

    public double confidenceLo() {
        return mean - (CONFIDENCE_INTERVAL_95 * (stddev / Math.sqrt(t)));
    }                  // low  endpoint of 95% confidence interval

    public double confidenceHi() {
        return mean + (CONFIDENCE_INTERVAL_95 * (stddev / Math.sqrt(t)));
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percStats = new PercolationStats(n, trials);
        StdOut.println("Mean: " + percStats.mean());
        StdOut.println("Stddev: " + percStats.stddev());
        StdOut.println("95% confidence interval [" + percStats.confidenceLo() +
                               ", " + percStats.confidenceHi() + "]");
    }
}
