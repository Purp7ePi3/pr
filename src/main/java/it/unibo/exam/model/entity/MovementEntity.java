package it.unibo.exam.model.entity;

import it.unibo.exam.utility.geometry.Point2D;

/**
 * A simple entity that can move in the environment.
 * Fixed version with proper hitbox updates.
 */
public class MovementEntity extends Entity {

    private static final int DEFAULT_SPEED = 10;
    private static final int DEFAULT_SIZE  = 800;
    private static final int MIN_SPEED     = 5;

    private int speed;

    /**
     * Constructs a MovementEntity placed at the center of the environment.
     * Calculates its initial speed based on the environment’s width.
     *
     * @param environmentSize the size of the environment
     */
    public MovementEntity(final Point2D environmentSize) {
        super(environmentSize);
        this.speed = calculateSpeed(environmentSize);
    }

    /**
     * Constructs a MovementEntity placed at a given starting position.
     * Calculates its initial speed based on the environment’s width.
     *
     * @param initialPosition the starting position of the entity
     * @param environmentSize the size of the environment
     */
    public MovementEntity(final Point2D initialPosition, final Point2D environmentSize) {
        super(initialPosition, environmentSize);
        this.speed = calculateSpeed(environmentSize);
    }

    /**
     * Calculates the movement speed based on environment width.
     *
     * @param environmentSize the size of the environment
     * @return the computed speed (at least {@code MIN_SPEED})
     */
    private int calculateSpeed(final Point2D environmentSize) {
        return Math.max(MIN_SPEED,
                        environmentSize.getX() / DEFAULT_SIZE * DEFAULT_SPEED);
    }

    /**
     * @return the current speed of the entity
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Moves the entity by the specified delta vector.
     * Updates the hitbox position accordingly.
     *
     * @param d the delta by which to move the entity
     */
    public void move(final Point2D d) {
        this.getPosition().move(d.getX(), d.getY());
        this.updateHitboxPosition();
    }

    /**
     * Moves the entity by the specified x and y distances.
     * Updates the hitbox position accordingly.
     *
     * @param dx the distance to move in the x direction
     * @param dy the distance to move in the y direction
     */
    public void move(final int dx, final int dy) {
        this.getPosition().move(dx, dy);
        this.updateHitboxPosition();
    }

    /**
     * Sets the entity’s position to the given point.
     * Updates the hitbox position accordingly.
     *
     * @param position the new position of the entity
     */
    public void setPosition(final Point2D position) {
        this.getPosition().setXY(position.getX(), position.getY());
        this.updateHitboxPosition();
    }

    /**
     * Sets the entity’s position to the given coordinates.
     * Updates the hitbox position accordingly.
     *
     * @param x the new x-coordinate
     * @param y the new y-coordinate
     */
    public void setPosition(final int x, final int y) {
        this.getPosition().setXY(x, y);
        this.updateHitboxPosition();
    }

    /**
     * Sets only the x-coordinate of the entity’s position.
     * Updates the hitbox position accordingly.
     *
     * @param x the new x-coordinate
     */
    public void setPositionX(final int x) {
        this.getPosition().setXY(x, this.getPosition().getY());
        this.updateHitboxPosition();
    }

    /**
     * Sets only the y-coordinate of the entity’s position.
     * Updates the hitbox position accordingly.
     *
     * @param y the new y-coordinate
     */
    public void setPositionY(final int y) {
        this.getPosition().setXY(this.getPosition().getX(), y);
        this.updateHitboxPosition();
    }

    /**
     * Resizes the entity to fit a new environment size and recalculates speed.
     *
     * @param newSize the new size of the environment
     */
    @Override
    public void resize(final Point2D newSize) {
        super.resize(newSize);
        this.speed = calculateSpeed(newSize);
    }
}
