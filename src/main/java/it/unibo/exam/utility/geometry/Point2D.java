package it.unibo.exam.utility.geometry;

/**
 * A simple class that rappresent a point in an 2D enviroment.
 */
public class Point2D {
    private int x;
    private int y;

    /**
     * Constructor for Point2D.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public Point2D(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Copy constructor.
     * @param p point to copy
     */
    public Point2D(final Point2D p) {
        this.x = p.x;
        this.y = p.y;
    }

    /**
     * @return the x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * @return the y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * @param dx the x coordinate to add
     * @param dy the y coordinate to add
     */
    public void move(final int dx, final int dy) {
        this.x += dx;
        this.y += dy;
    }

    /**
     * @param x the x coordinate to set
     * @param y the y coordinate to set
     */
    public void setXY(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @param p the point to calculate the distance to
     * @return the distance from this point to the point p
     */
    public double distance(final Point2D p) {
        return Math.sqrt(
            Math.pow(this.x - p.getX(), 2) + Math.pow(this.y - p.getY(), 2)
        );
    }
}
