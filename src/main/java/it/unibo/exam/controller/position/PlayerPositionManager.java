package it.unibo.exam.controller.position;

import it.unibo.exam.model.entity.Player;
import it.unibo.exam.model.entity.enviroments.Door;
import it.unibo.exam.utility.geometry.Point2D;

/**
 * Manages player positioning when transitioning between rooms.
 * Improved version with better spawn position calculations.
 */
public final class PlayerPositionManager {

    private static final int SPAWN_OFFSET = 80; // Increased distance from door when spawning

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private  PlayerPositionManager() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Positions the player appropriately when entering a new room through a door.
     * 
     * @param player the player to position
     * @param door the door used to enter the room
     * @param environmentSize the size of the current environment
     */
    public static void positionPlayerAfterTransition(final Player player, final Door door, final Point2D environmentSize) {
        final Point2D doorPosition = door.getPosition();
        final Point2D doorDimension = door.getDimension();
        final Point2D newPosition = calculateSpawnPosition(doorPosition, doorDimension, environmentSize);

        player.setPosition(newPosition);

    }

    /**
     * Calculates the spawn position based on door location and room layout.
     * 
     * @param doorPosition the position of the door
     * @param doorDimension the dimensions of the door
     * @param environmentSize the size of the environment
     * @return the calculated spawn position
     */
    private static Point2D calculateSpawnPosition(final Point2D doorPosition,
                            final Point2D doorDimension, final Point2D environmentSize) {
        final int doorCenterX = doorPosition.getX() + doorDimension.getX() / 2;
        final int doorCenterY = doorPosition.getY() + doorDimension.getY() / 2;

        // Player dimensions (same calculation as in Entity)
        final int playerWidth = environmentSize.getX() / 20;
        final int playerHeight = environmentSize.getY() / 20;

        // Determine which edge of the screen the door is closest to
        final int distanceToLeft = doorPosition.getX();
        final int distanceToRight = environmentSize.getX() - (doorPosition.getX() + doorDimension.getX());
        final int distanceToTop = doorPosition.getY();
        final int distanceToBottom = environmentSize.getY() - (doorPosition.getY() + doorDimension.getY());

        // Find the minimum distance to determine which edge the door is on
        final int minDistance = Math.min(Math.min(distanceToLeft, distanceToRight), 
                                        Math.min(distanceToTop, distanceToBottom));

        int spawnX, spawnY;

        if (minDistance == distanceToLeft) {
            // Door is on left edge, spawn to the right of it
            spawnX = doorPosition.getX() + doorDimension.getX() + SPAWN_OFFSET;
            spawnY = doorCenterY - playerHeight / 2;
        } else if (minDistance == distanceToRight) {
            // Door is on right edge, spawn to the left of it
            spawnX = doorPosition.getX() - playerWidth - SPAWN_OFFSET;
            spawnY = doorCenterY - playerHeight / 2;
        } else if (minDistance == distanceToTop) {
            // Door is on top edge, spawn below it
            spawnX = doorCenterX - playerWidth / 2;
            spawnY = doorPosition.getY() + doorDimension.getY() + SPAWN_OFFSET;
        } else { // minDistance == distanceToBottom
            // Door is on bottom edge, spawn above it
            spawnX = doorCenterX - playerWidth / 2;
            spawnY = doorPosition.getY() - playerHeight - SPAWN_OFFSET;
        }

        // Ensure spawn position is within bounds with safety margins
        final int margin = 20;
        spawnX = Math.max(margin, Math.min(spawnX, environmentSize.getX() - playerWidth - margin));
        spawnY = Math.max(margin, Math.min(spawnY, environmentSize.getY() - playerHeight - margin));

        return new Point2D(spawnX, spawnY);
    }

    /**
     * Gets the default spawn position for a room (typically the center).
     * 
     * @param environmentSize the size of the environment
     * @return the default spawn position
     */
    public static Point2D getDefaultSpawnPosition(final Point2D environmentSize) {
        final int playerWidth = environmentSize.getX() / 20;
        final int playerHeight = environmentSize.getY() / 20;

        return new Point2D(
            environmentSize.getX() / 2 - playerWidth / 2, 
            environmentSize.getY() / 2 - playerHeight / 2
        );
    }
}
