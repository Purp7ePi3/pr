package it.unibo.exam.model.entity;

import it.unibo.exam.utility.geometry.Point2D;

/**
 * A simple entity that can move in the environment.
 */
public class MovementEntity extends Entity {

    private static final int DEFAULT_SPEED = 20;
    private static final int DEFAULT_SIZE = 800;

    private int speed;

    /**
     * Constructor.
     * Creates an entity and updates speed according to the environment size.
     *
     * @param environmentSize the size of the environment
     * @see Entity
     */
    public MovementEntity(final Point2D environmentSize) {
        super(environmentSize);
        this.speed = this.getEnviromentSize().getX() / DEFAULT_SIZE * DEFAULT_SPEED;
    }

    /**
     * Updates speed according to the environment size.
     *
     * @return the updated speed
     * @Note Final keyword is necessary
     */
    private int updateSpeed() {
        return this.getEnviromentSize().getX() / DEFAULT_SIZE * DEFAULT_SPEED;
    }

    /**
     * @return the speed of the entity
    */
    public int getSpeed() {
        return speed;
    }

    /**
     * Moves the entity by the specified distance.
     *
     * @param d the distance to move
     */
    public void move(final Point2D d) {
        this.getPosition().move(d.getX(), d.getY());
    }

    /**
     * Moves the entity by the specified distance.
     *
     * @param dx the distance to move in the x direction
     * @param dy the distance to move in the y direction
     */
    public void move(final int dx, final int dy) {
        this.getPosition().move(dx, dy);
    }

    /**
     * Sets the position of the entity.
     *
     * @param position the new position of the entity
     */
    public void setPosition(final Point2D position) {
        this.getPosition().setXY(position.getX(), position.getY());
    }

    /**
     * Sets the position of the entity.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @see Point2D#setXY(int, int)
     */
    public void setPosition(final int x, final int y) {
        this.getPosition().setXY(x, y);
    }

    /**
     * Sets the x-coordinate of the entity's position.
     *
     * @param x the x coordinate
     * @see Point2D#setXY(int, int)
     */
    public void setPositionX(final int x) {
        this.getPosition().setXY(x, this.getPosition().getY());
    }

    /**
     * Sets the y-coordinate of the entity's position.
     *
     * @param y the y coordinate
     * @see Point2D#setXY(int, int)
     */
    public void setPositionY(final int y) {
        this.getPosition().setXY(this.getPosition().getX(), y);
    }

    /**
     * Resizes the entity and updates its speed.
     *
     * @param newSize the new size of the environment
     */
    @Override
    public void resize(final Point2D newSize) {
        super.resize(newSize);
        this.speed = updateSpeed();
    }
}
