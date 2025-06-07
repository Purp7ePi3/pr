package it.unibo.exam.controller.position;

import it.unibo.exam.model.entity.Player;
import it.unibo.exam.model.entity.enviroments.Door;
import it.unibo.exam.utility.geometry.Point2D;

/**
 * Manages player positioning when transitioning between rooms.
 * Calculates appropriate spawn positions based on door directions.
 */
public class PlayerPositionManager {
    
    private static final int SPAWN_OFFSET = 50; // Distance from door when spawning
    
    /**
     * Positions the player appropriately when entering a new room through a door.
     * 
     * @param player the player to position
     * @param door the door used to enter the room
     * @param environmentSize the size of the current environment
     */
    public static void positionPlayerAfterTransition(Player player, Door door, Point2D environmentSize) {
        final Point2D doorPosition = door.getPosition();
        final Point2D doorDimension = door.getDimension();
        final Point2D newPosition = calculateSpawnPosition(doorPosition, doorDimension, environmentSize);
        
        player.setPosition(newPosition);
    }
    
    /**
     * Calculates the spawn position based on door location and room type.
     * 
     * @param doorPosition the position of the door
     * @param doorDimension the dimensions of the door
     * @param environmentSize the size of the environment
     * @return the calculated spawn position
     */
    private static Point2D calculateSpawnPosition(Point2D doorPosition, Point2D doorDimension, Point2D environmentSize) {
        final int doorCenterX = doorPosition.getX() + doorDimension.getX() / 2;
        final int doorCenterY = doorPosition.getY() + doorDimension.getY() / 2;
        
        // Determine which edge of the screen the door is on
        final boolean isOnLeftEdge = doorPosition.getX() <= SPAWN_OFFSET;
        final boolean isOnRightEdge = doorPosition.getX() >= environmentSize.getX() - SPAWN_OFFSET;
        final boolean isOnTopEdge = doorPosition.getY() <= SPAWN_OFFSET;
        final boolean isOnBottomEdge = doorPosition.getY() >= environmentSize.getY() - SPAWN_OFFSET;
        
        int spawnX, spawnY;
        
        if (isOnLeftEdge) {
            // Door is on left edge, spawn to the right of it
            spawnX = doorPosition.getX() + doorDimension.getX() + SPAWN_OFFSET;
            spawnY = doorCenterY;
        } else if (isOnRightEdge) {
            // Door is on right edge, spawn to the left of it
            spawnX = doorPosition.getX() - SPAWN_OFFSET;
            spawnY = doorCenterY;
        } else if (isOnTopEdge) {
            // Door is on top edge, spawn below it
            spawnX = doorCenterX;
            spawnY = doorPosition.getY() + doorDimension.getY() + SPAWN_OFFSET;
        } else if (isOnBottomEdge) {
            // Door is on bottom edge, spawn above it
            spawnX = doorCenterX;
            spawnY = doorPosition.getY() - SPAWN_OFFSET;
        } else {
            // Door is somewhere in the middle, default to center spawn
            spawnX = environmentSize.getX() / 2;
            spawnY = environmentSize.getY() / 2;
        }
        
        // Ensure spawn position is within bounds
        spawnX = Math.max(SPAWN_OFFSET, Math.min(spawnX, environmentSize.getX() - SPAWN_OFFSET));
        spawnY = Math.max(SPAWN_OFFSET, Math.min(spawnY, environmentSize.getY() - SPAWN_OFFSET));
        
        return new Point2D(spawnX, spawnY);
    }
    
    /**
     * Gets the default spawn position for a room (typically the center).
     * 
     * @param environmentSize the size of the environment
     * @return the default spawn position
     */
    public static Point2D getDefaultSpawnPosition(Point2D environmentSize) {
        return new Point2D(environmentSize.getX() / 2, environmentSize.getY() / 2);
    }
    
    /**
     * Positions the player at the opposite side of the room from where they entered.
     * Useful for creating more natural room transitions.
     * 
     * @param player the player to position
     * @param entryDoor the door the player used to enter
     * @param targetDoor the door that corresponds to the entry door in the new room
     * @param environmentSize the size of the environment
     */
    public static void positionPlayerOppositeToEntry(Player player, Door entryDoor, Door targetDoor, Point2D environmentSize) {
        if (targetDoor != null) {
            // Position player near the target door (opposite side)
            positionPlayerAfterTransition(player, targetDoor, environmentSize);
        } else {
            // Fallback to default positioning
            player.setPosition(getDefaultSpawnPosition(environmentSize));
        }
    }
}