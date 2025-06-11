package it.unibo.exam.model.entity.enviroments;

import it.unibo.exam.model.entity.Entity;
import it.unibo.exam.utility.geometry.Point2D;

/**
 * Simple door class.
 */
public class Door extends Entity {

    private boolean isOpen;
    private final int fromId;
    private final int toId;

    /**
     * Constructor for Door.
     *
     * @param position the inizial position of the door
     * @param enviromentSize the dimension of the enviroment
     * @param fromId current room index
     * @param toId next room index
     */
    public Door(final Point2D enviromentSize, final Point2D position, final int fromId, final int toId) {
        super(position, enviromentSize);
        this.fromId = fromId;
        this.toId = toId;
        this.isOpen = false;
    }

    /**
     * @return if the door is open or closed
     */
    public boolean isOpen() {
        return this.isOpen;
    }

    /**
     * Sets the door to open.
     */
    public void open() {
        this.isOpen = true;
    }

    /**
     * Sets the door to closed.
     */
    public void close() {
        this.isOpen = false;
    }

    /**
     * @return the id of the door
     */
    public int getFromId() {
        return this.fromId;
    }

    /**
     * @return the id of the door
     */
    public int getToId() {
        return this.toId;
    }

}
