package it.unibo.exam.view.renderer;

import it.unibo.exam.model.entity.Entity;
import it.unibo.exam.model.entity.Player;
import it.unibo.exam.utility.geometry.Point2D;

import java.awt.Graphics2D;
import java.awt.Color;

/**
 * Renderer for the player entity.
 */
public class PlayerRenderer extends EntityRenderer {

    private static final Color PLAYER_COLOR = new Color(70, 130, 180); // Steel Blue
    private static final Color PLAYER_BORDER_COLOR = new Color(25, 25, 112); // Midnight Blue

    /**
     * Renders the player as a colored rectangle with "P" text.
     * 
     * @param g the graphics context
     * @param entity the player entity to render
     */
    @Override
    public void render(final Graphics2D g, final Entity entity) {
        if (!(entity instanceof Player)) {
            throw new IllegalArgumentException("Entity must be a Player instance");
        }

        final Player player = (Player) entity;
        final Point2D position = player.getPosition();
        final Point2D dimension = player.getDimension();

        // Draw player body
        g.setColor(PLAYER_COLOR);
        g.fillRect(
            position.getX(), 
            position.getY(), 
            dimension.getX(), 
            dimension.getY()
        );

        // Draw border
        g.setColor(PLAYER_BORDER_COLOR);
        g.drawRect(
            position.getX(), 
            position.getY(), 
            dimension.getX(), 
            dimension.getY()
        );

        // Draw "P" in the center
        drawCenteredText(g, player, "P", Color.WHITE);

        // Optional: Draw direction indicator (small arrow based on last movement)
        drawDirectionIndicator(g, player);
    }

    /**
     * Draws a small direction indicator on the player.
     * 
     * @param g the graphics context
     * @param player the player
     */
    private void drawDirectionIndicator(final Graphics2D g, final Player player) {
        final Point2D position = player.getPosition();
        final Point2D dimension = player.getDimension();

        // Simple arrow pointing up (can be enhanced to show actual direction)
        final int centerX = position.getX() + dimension.getX() / 2;
        final int centerY = position.getY() + dimension.getY() / 4;

        g.setColor(Color.YELLOW);
        g.fillOval(centerX - 2, centerY - 2, 4, 4);
    }
}
