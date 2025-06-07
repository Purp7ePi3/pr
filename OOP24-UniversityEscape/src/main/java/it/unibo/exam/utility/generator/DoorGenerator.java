package it.unibo.exam.utility.generator;

import java.util.List;

import it.unibo.exam.model.entity.enviroments.Door;
import it.unibo.exam.utility.geometry.Point2D;

/**
 * A generator class for doors.
 */
public class DoorGenerator extends EntityGenerator<List<Door>> {
    private final List<Point2D> dir;

    /**
     * @param enviromentSize
     */
    public DoorGenerator(final Point2D enviromentSize) {
        super(enviromentSize);
        dir = List.of(
            new Point2D(enviromentSize.getX(), enviromentSize.getY() / 2), // Est
            new Point2D(enviromentSize.getX() / 2, enviromentSize.getY()), // SUD
            new Point2D(0, enviromentSize.getY() / 2), // Ovest
            new Point2D(enviromentSize.getX() / 2, 0) // Nord
        );
    }

    /**
     * @param id id of the room
     * @return List of doors in the room
     */
    @Override
    public List<Door> generate(final int id) {
        switch (id) {
            case 0 : {
                return List.of(
                    generateSingleDoor(0, 1),
                    generateSingleDoor(0, 2),
                    generateSingleDoor(0, 3),
                    generateSingleDoor(0, 4)
                );
            }
            case 1 :
            case 2 :
            case 3 :
            case 4 : {
                return List.of(
                    generateSingleDoor(id, 0)
                );
            }
            default : 
                throw new IllegalArgumentException("Id must be in [0,4]");
        }
    }

    /**
     * @param fromId Id of from room
     * @param toId Id of to room
     * @return Door from room (fromId) to room (toId)
     */
    private Door generateSingleDoor(final int fromId, final int toId) {
        return new Door(
            super.getEnv(), 
            getPosition(fromId, toId), 
            fromId, 
            toId
        );
    }

    /**
     * @param fromId Id of from room
     * @param toId Id of to room
     * @return Position of the door from room (fromId) to room (toId)
     */
    private Point2D getPosition(final int fromId, final int toId) {
        return fromId == 0 ? dir.get(toId) : dir.get((toId + 2) % 4);
    }




}
