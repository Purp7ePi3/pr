package it.unibo.exam.view.renderer;

import it.unibo.exam.model.entity.Entity;
import it.unibo.exam.utility.geometry.Point2D;
import it.unibo.exam.utility.geometry.Rectangle;

import java.awt.Graphics2D;
import java.awt.Color;

/**
 * Base class for rendering entities with common functionality.
 */
public abstract class EntityRenderer {

    /**
     * Renders an entity with the specified graphics context.
     * 
     * @param g the graphics context
     * @param entity the entity to render
     */
    public abstract void render(Graphics2D g, Entity entity);

    /**
     * Draws a rectangle based on entity's hitbox.
     * 
     * @param g the graphics context
     * @param entity the entity
     * @param color the color to fill the rectangle
     */
    protected void drawEntityRectangle(final Graphics2D g, final Entity entity, final Color color) {
        final Rectangle hitbox = entity.getHitbox(); //NOPMD
        final Point2D position = entity.getPosition();
        final Point2D dimension = entity.getDimension();

        g.setColor(color);
        g.fillRect(
            position.getX(), 
            position.getY(), 
            dimension.getX(), 
            dimension.getY()
        );

        // Draw border
        g.setColor(Color.BLACK);
        g.drawRect(
            position.getX(), 
            position.getY(), 
            dimension.getX(), 
            dimension.getY()
        );
    }

    /**
     * Draws text centered on the entity.
     * 
     * @param g the graphics context
     * @param entity the entity
     * @param text the text to draw
     * @param color the text color
     */
    protected void drawCenteredText(final Graphics2D g, final Entity entity, final String text, final Color color) {
        final Point2D position = entity.getPosition();
        final Point2D dimension = entity.getDimension();

        g.setColor(color);

        // Calculate text position to center it
        final int textX = position.getX() + dimension.getX() / 2 - (text.length() * 3);
        final int textY = position.getY() + dimension.getY() / 2 + 5;

        g.drawString(text, textX, textY);
    }
}
