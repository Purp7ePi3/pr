package it.unibo.exam.utility.generator;

import java.util.List;

import it.unibo.exam.model.entity.enviroments.Door;
import it.unibo.exam.utility.geometry.Point2D;

/**
 * A generator class for doors.
 * Fixed version with proper door positioning and collision detection.
 */
public class DoorGenerator extends EntityGenerator<List<Door>> {
    private List<Point2D> dir;
    private final Point2D environmentSize;

    /**
     * @param enviromentSize
     */
    public DoorGenerator(final Point2D enviromentSize) {
        super(enviromentSize);
        this.environmentSize = new Point2D(enviromentSize);
        calculateDoorPositions(enviromentSize);
    }

    /**
     * Calculates door positions based on environment size.
     * @param enviromentSize the environment size
     */
    private void calculateDoorPositions(final Point2D enviromentSize) {
        // Calculate door dimensions (same as entity dimensions)
        final int doorWidth = Math.max(40, enviromentSize.getX() / 20);  // Minimum 40px width
        final int doorHeight = Math.max(40, enviromentSize.getY() / 20); // Minimum 40px height

        // Position doors with proper bounds checking
        final int margin = 20; // Reduced margin for better positioning

        dir = List.of(
            // To room 1 - Right side
            new Point2D(
                Math.max(0, enviromentSize.getX() - doorWidth - margin), 
                Math.max(0, (enviromentSize.getY() - doorHeight) / 2)
            ),
            // To room 2 - Bottom side
            new Point2D(
                Math.max(0, (enviromentSize.getX() - doorWidth) / 2), 
                Math.max(0, enviromentSize.getY() - doorHeight - margin)
            ),
            // To room 3 - Left side
            new Point2D(
                margin, 
                Math.max(0, (enviromentSize.getY() - doorHeight) / 2)
            ),
            // To room 4 - Top side
            new Point2D(
                Math.max(0, (enviromentSize.getX() - doorWidth) / 2), 
                margin
            )
        );
    }

    /**
     * Updates door positions when environment is resized.
     * @param newSize the new environment size
     */
    public void updateEnvironmentSize(final Point2D newSize) {
        this.environmentSize.setXY(newSize.getX(), newSize.getY());
        calculateDoorPositions(newSize);
    }

    /**
     * @param id
     * Updates the doors in this room.
     * @implNote call only after room initialization is complete.
     */
    @Override
    public final List<Door> generate(final int id) {
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
        final Point2D position = getPosition(fromId, toId);

        // Validate position is within bounds
        final int doorWidth = Math.max(40, environmentSize.getX() / 20);
        final int doorHeight = Math.max(40, environmentSize.getY() / 20);

        final int validX = Math.max(0, Math.min(position.getX(), environmentSize.getX() - doorWidth));
        final int validY = Math.max(0, Math.min(position.getY(), environmentSize.getY() - doorHeight));

        final Point2D validPosition = new Point2D(validX, validY);

        return new Door(
            super.getEnv(), 
            validPosition, 
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
        if (fromId == 0) {
            // From main room (0) to other rooms (1-4)
            final int dirIndex = toId - 1; // Convert toId (1-4) to dirIndex (0-3)
            if (dirIndex < 0 || dirIndex >= dir.size()) {
                throw new IllegalArgumentException("Invalid toId: " + toId + " for fromId: " + fromId);
            }
            return new Point2D(dir.get(dirIndex));
        } else {
            // From other rooms (1-4) back to main room (0)
            // Use the same position as the corresponding door in main room
            final int dirIndex = fromId - 1; // Room 1 uses index 0, room 2 uses index 1, etc.
            if (dirIndex < 0 || dirIndex >= dir.size()) {
                throw new IllegalArgumentException("Invalid fromId: " + fromId + " for toId: " + toId);
            }
            return new Point2D(dir.get(dirIndex));
        }
    }
}
