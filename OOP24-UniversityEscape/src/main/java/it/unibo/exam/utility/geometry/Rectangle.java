package it.unibo.exam.utility.geometry;

/**
 * A simple class that rappresent a rectangle.
 */
public class Rectangle {

    private final Point2D p;
    private final Point2D d;

    /**
     * @param p top-left corner
     * @param d size
    */
    public Rectangle(final Point2D p, final Point2D d) {
        if (d == null || p == null) {
            throw new IllegalArgumentException("position and size cannot be null");
        }
        this.d = new Point2D(d);
        this.p = new Point2D(p);
    }

    /**
     * @param other other rectangle
     * @return True if the rectangle intersects with another rectangle
     *        False otherwise
     */
    public boolean intersects(final Rectangle other)  {

        final int x = this.p.getX();
        final int y = this.p.getY();
        final int width = this.d.getX();
        final int height = this.d.getY();

        final int otherX = other.p.getX();
        final int otherY = other.p.getY();
        final int otherWidth = other.d.getX();
        final int otherHeight = other.d.getY();


        return x < otherX + otherWidth 
            && x + width > otherX 
            && y < otherY + otherHeight 
            && y + height > otherY;
    }

    /**
     * @param point 2d point
     * @return True if the rectangle contain point
     *         False otherwise
     */
    public boolean contains(final Point2D point) {
        return point.getX() >= p.getX() && point.getX() <= p.getX() + d.getX() 
            && point.getY() >= p.getY() && point.getY() <= p.getY() + d.getY();
    }

}
