/**
 * Name: Pecolation Date: 20/04/2019
 *
 * @author mbadaz (Tapiwa Muzira)
 * <p>
 * Description: {@link Percolation} is a data type that models a percolation system through an N*N
 * sized grid that is based on the Weighted Quick Union data structure.
 * <p>
 * A system percolates if there is a path of connected open sites that runs from one end (top/start)
 * of the system to the other end(bottom/end) of the system.
 * <p>
 * For example: A pourous material percolates if a liquid (e.g) water flow from the top through to
 * the bottom of the material, i.e there is a path of open/unblocked sites that runs trough the
 * material A composite material made of conducting and non-conducting material percolates if an
 * electric current can flow from the top/start of the material to the bottom/end of the material,
 * i.e there is a path of connected conducting materials that runs from the one end of the material
 * to the other end.
 * <p>
 * ***************************************************************************
 */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF wqUF;  // the Weighted Quick Union Algorithm
    private boolean[] sitesStates;          // holds the states of the sites i.e blocked / open
    private final int gridDimenSize;
    private final int virtualTop;
    private final int virtualBottom;
    private int openSites = 0;

    /**
     * @param n The grid dimension size
     */
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        wqUF = new WeightedQuickUnionUF(n * n);
        gridDimenSize = n;
        sitesStates = new boolean[n * n];
        virtualTop = 0;
        virtualBottom = n * n - 1;
        for (int i = 0; i < n; i++) {
            sitesStates[i] = false;
        }
    }

    /**
     * Opens a blocked site at the given coordinates
     *
     * @param row is the y coordinate
     * @param col is the x coordinates
     */
    public void open(int row, int col) {
        int site = coordinatesToSiteId(row, col);
        if (isOpen(row, col)) return;
        connectSite(site);
        openSites++;
    }

    /**
     * Calculates the site index ID from coordinates
     *
     * @param row is the y coordinate
     * @param col is the x coordinate
     */
    private int coordinatesToSiteId(int row, int col) {
        int site = (gridDimenSize * (row - 1)) + (col - 1);
        if (site < 0) throw new IllegalArgumentException();
        return site;
    }

    /**
     * Checks if a sites is opened
     *
     * @param row is the y coordinate
     * @param col is the x coordinate
     */
    public boolean isOpen(int row, int col) {
        return sitesStates[coordinatesToSiteId(row, col)];
    }

    /**
     * Checks to see whether a site is connected to the top ({@link Percolation#virtualTop})
     *
     * @param row is the y coordinate
     * @param col is the x coordinate
     * @return boolean
     */
    public boolean isFull(int row, int col) {
        int site = coordinatesToSiteId(row, col);
        return wqUF.find(site) == wqUF.find(virtualTop) && sitesStates[site ];
    }

    /**
     * Counts the number of open sites
     *
     * @return number of open sites
     */
    public int numberOfOpenSites() {
        return openSites;
    }

    /**
     * Checks whether the system percolates
     *
     * @return boolean percolation state
     */
    public boolean percolates() {
        return wqUF.find(virtualTop) == wqUF.find(virtualBottom);
    }

    /**
     * Connects the site to all of it's adjacent open sites.
     *
     * @param site site to be connected
     */
    private void connectSite(int site) {
        if (sitesStates[site]) return;

        // calculate the adjacent sites
        int siteAbove = site - gridDimenSize;
        int siteRight = site + 1;
        int siteBelow = site + gridDimenSize;
        int siteLeft = site - 1;


        if (site % gridDimenSize != 0 && sitesStates[siteLeft]) {
            // connect to left site
            wqUF.union(site, siteLeft);
        }

        if ((site + 1) % gridDimenSize != 0 && sitesStates[siteRight]) {
            // connect to right site
            wqUF.union(site, siteRight);
        }

        if (siteAbove >= 0 && sitesStates[siteAbove]) {
            // connect to site above
            wqUF.union(site, siteAbove);
        }
        else if (siteAbove < 0) {
            // site is in top row so connect to virtual top site
            if (site != virtualTop) wqUF.union(site, virtualTop);
        }

        if (siteBelow < gridDimenSize * gridDimenSize - 1 && sitesStates[siteBelow]) {
            // connect to below site
            wqUF.union(site, siteBelow);
        }
        else if (siteBelow >= gridDimenSize * gridDimenSize) {
            // site is in bottom row so connect to virtual bottom site
            if (site != virtualBottom) wqUF.union(site, virtualBottom);
        }

        sitesStates[site] = true;
    }

}
