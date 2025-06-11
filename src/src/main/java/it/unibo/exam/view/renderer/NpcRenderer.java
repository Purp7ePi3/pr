package it.unibo.exam.view.renderer;

import it.unibo.exam.model.entity.Entity;
import it.unibo.exam.model.entity.Npc;
import it.unibo.exam.utility.geometry.Point2D;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;

/**
 * Renderer for NPC entities.
 */
public class NpcRenderer extends EntityRenderer {

    private static final Color NPC_COLOR = new Color(255, 165, 0); // Orange
    private static final Color NPC_BORDER_COLOR = new Color(255, 140, 0); // Dark Orange
    private static final Color INTERACTION_INDICATOR_COLOR = new Color(255, 255, 0); // Yellow
    private static final Color BACKGROUND_COLOR = new Color(0, 0, 0, 128);
    /**
     * Renders an NPC with interaction indicators.
     * 
     * @param g the graphics context
     * @param entity the NPC entity to render
     */
    @Override
    public void render(final Graphics2D g, final Entity entity) {
        if (!(entity instanceof Npc)) {
            throw new IllegalArgumentException("Entity must be an Npc instance");
        }

        final Npc npc = (Npc) entity;
        final Point2D position = npc.getPosition();
        final Point2D dimension = npc.getDimension();

        // Draw NPC body
        g.setColor(NPC_COLOR);
        g.fillRect(
            position.getX(), 
            position.getY(), 
            dimension.getX(), 
            dimension.getY()
        );

        // Draw border
        g.setColor(NPC_BORDER_COLOR);
        g.drawRect(
            position.getX(), 
            position.getY(), 
            dimension.getX(), 
            dimension.getY()
        );

        // Draw "N" in the center
        drawCenteredText(g, npc, "N", Color.WHITE);

        // Draw interaction indicator
        drawInteractionIndicator(g, npc);

        // Draw name above NPC
        drawNpcName(g, npc);
    }

    /**
     * Draws an interaction indicator above the NPC.
     * 
     * @param g the graphics context
     * @param npc the NPC
     */
    private void drawInteractionIndicator(final Graphics2D g, final Npc npc) {
        final Point2D position = npc.getPosition();
        final Point2D dimension = npc.getDimension();

        // Draw "E" above the NPC to indicate interaction key
        g.setColor(INTERACTION_INDICATOR_COLOR);
        final int indicatorX = position.getX() + dimension.getX() / 2 - 5;
        final int indicatorY = position.getY() - 5;

        // Draw small background circle
        final int height = 12;
        g.fillOval(indicatorX - 3, indicatorY - 8, 16, height);

        // Draw "E" text
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 10));
        g.drawString("E", indicatorX, indicatorY);
    }

    /**
     * Draws the NPC's name above the entity.
     * 
     * @param g the graphics context
     * @param npc the NPC
     */
    private void drawNpcName(final Graphics2D g, final Npc npc) {
        final Point2D position = npc.getPosition();
        final Point2D dimension = npc.getDimension();

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 8));

        // Truncate name if too long
        String displayName = npc.getName();
        final int lastIndex = 9, max = 12;
        if (displayName.length() > max) {
            displayName = displayName.substring(0, lastIndex) + "...";
        }

        final int nameX = position.getX() + dimension.getX() / 2 - (displayName.length() * 2);
        final int nameY = position.getY() - 15;

        // Draw background for text
        g.setColor(BACKGROUND_COLOR);
        final int height = 9;
        g.fillRect(nameX - 2, nameY - 10, displayName.length() * 4 + 4, height);

        // Draw text
        g.setColor(Color.WHITE);
        g.drawString(displayName, nameX, nameY);
    }
}
