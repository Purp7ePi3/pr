package it.unibo.exam.view.renderer;

import it.unibo.exam.model.entity.Entity;
import it.unibo.exam.model.entity.enviroments.Door;
import it.unibo.exam.utility.geometry.Point2D;

import java.awt.Graphics2D;
import java.awt.Color;

/**
 * Renderer for door entities.
 */
public class DoorRenderer extends EntityRenderer {

    private static final Color DOOR_OPEN_COLOR = new Color(34, 139, 34); // Forest Green
    private static final Color DOOR_CLOSED_COLOR = new Color(139, 69, 19); // Saddle Brown
    private static final Color DOOR_BORDER_COLOR = new Color(85, 107, 47); // Dark Olive Green

    /**
     * Renders a door with different colors based on its state.
     * 
     * @param g the graphics context
     * @param entity the door entity to render
     */
    @Override
    public void render(final Graphics2D g, final Entity entity) {
        if (!(entity instanceof Door)) {
            throw new IllegalArgumentException("Entity must be a Door instance");
        }

        final Door door = (Door) entity;
        final Point2D position = door.getPosition();
        final Point2D dimension = door.getDimension();

        // Choose color based on door state
        final Color doorColor = door.isOpen() ? DOOR_OPEN_COLOR : DOOR_CLOSED_COLOR;

        // Draw door body
        g.setColor(doorColor);
        g.fillRect(
            position.getX(), 
            position.getY(), 
            dimension.getX(), 
            dimension.getY()
        );

        // Draw border
        g.setColor(DOOR_BORDER_COLOR);
        g.drawRect(
            position.getX(), 
            position.getY(), 
            dimension.getX(), 
            dimension.getY()
        );

        // Draw door identifier
        final String doorText = door.isOpen() ? "D" : "D";
        drawCenteredText(g, door, doorText, Color.WHITE);

        // Draw room ID indicator
        drawRoomIdIndicator(g, door);

        // Draw door handle if closed
        if (!door.isOpen()) {
            drawDoorHandle(g, door);
        }
    }

    /**
     * Draws a small indicator showing which room this door leads to.
     * 
     * @param g the graphics context
     * @param door the door
     */
    private void drawRoomIdIndicator(final Graphics2D g, final Door door) {
        final Point2D position = door.getPosition();
        final Point2D dimension = door.getDimension();

        // Draw small number in corner
        g.setColor(Color.YELLOW);
        final String roomId = String.valueOf(door.getToId());
        final int offsetX = 15, offsetY = 12;
        g.drawString(roomId, 
            position.getX() + dimension.getX() - offsetX, 
            position.getY() + offsetY);
    }

    /**
     * Draws a door handle for closed doors.
     * 
     * @param g the graphics context
     * @param door the door
     */
    private void drawDoorHandle(final Graphics2D g, final Door door) {
        final Point2D position = door.getPosition();
        final Point2D dimension = door.getDimension();

        // Draw small handle
        g.setColor(Color.DARK_GRAY);
        final int handleX = position.getX() + dimension.getX() - 8;
        final int handleY = position.getY() + dimension.getY() / 2;
        g.fillOval(handleX, handleY - 2, 4, 4);
    }
}
