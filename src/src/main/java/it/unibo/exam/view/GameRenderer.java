package it.unibo.exam.view;

import it.unibo.exam.model.entity.Player;
import it.unibo.exam.model.entity.enviroments.Room;
import it.unibo.exam.model.game.GameState;
import it.unibo.exam.view.hud.ScoreHud;
import it.unibo.exam.view.renderer.PlayerRenderer;
import it.unibo.exam.view.renderer.DoorRenderer;
import it.unibo.exam.view.renderer.NpcRenderer;
import it.unibo.exam.utility.generator.RoomGenerator;

import java.awt.Graphics2D;
import java.awt.Color;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Handles rendering of the game elements such as rooms and players.
 * Updated with entity-specific renderers.
 */
public class GameRenderer {

    private static final Color MAIN_ROOM_COLOR = new Color(70, 70, 90);
    private static final Color PUZZLE_ROOM_COLOR = new Color(60, 80, 60);
    private static final Color DEFAULT_ROOM_COLOR = new Color(50, 50, 50);

    private final GameState gs;
    private final ScoreHud scoreHud;

    // Entity renderers
    private final PlayerRenderer playerRenderer;
    private final DoorRenderer doorRenderer;
    private final NpcRenderer npcRenderer;

    /**
     * Constructor for GameRenderer.
     *
     * @param gs the game state to render
     */
    public GameRenderer(final GameState gs) {
        this.gs = gs;
        this.scoreHud = new ScoreHud(gs);

        // Initialize renderers
        this.playerRenderer = new PlayerRenderer();
        this.doorRenderer = new DoorRenderer();
        this.npcRenderer = new NpcRenderer();
    }

    /**
     * Renders the game by rendering the current room and player.
     * This method should be called from a component with a Graphics2D context.
     * 
     * @param g the graphics context to render on
     */
    public void renderGame(final Graphics2D g) {
        if (g == null) {
            throw new IllegalArgumentException("Graphics context cannot be null");
        }

        renderRoom(g, gs.getCurrentRoom());
        renderPlayer(g, gs.getPlayer());
    }

    /**
     * Legacy method for backward compatibility.
     * @deprecated Use renderGame(Graphics2D) instead
     */
    @Deprecated
    public void renderGame() {
        // This method is deprecated but kept for backward compatibility
        // The actual rendering should be done externally with Graphics2D context
    }

    /**
     * Renders non-interactive overlays such as the score HUD.
     *
     * @param g the graphics context to draw on
     */
    public void renderHud(final Graphics2D g) {
        scoreHud.draw(g);
    }

    /** 
     * Renders the current room background, doors, and NPCs.
     *
     * @param g the graphics context
     * @param currentRoom the room to render
     */
    private void renderRoom(final Graphics2D g, final Room currentRoom) {
        if (currentRoom == null) {
            throw new IllegalArgumentException("Room cannot be null");
        }

        // Clear background
        clearBackground(g);

        // Draw room background
        drawRoomBackground(g, currentRoom);

        // Render all doors
        currentRoom.getDoors().forEach(door -> doorRenderer.render(g, door));

        // Render NPC if present and room has one
        if (currentRoom.getRoomType() == RoomGenerator.PUZZLE_ROOM && currentRoom.getNpc() != null) {
            npcRenderer.render(g, currentRoom.getNpc());
        }
    }

    /**
     * Renders the player.
     *
     * @param g the graphics context
     * @param player the player to render
     */
    private void renderPlayer(final Graphics2D g, final Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }

        playerRenderer.render(g, player);
    }

    /**
     * Clears the background with a default color.
     * 
     * @param g the graphics context
     */
    private void clearBackground(final Graphics2D g) {
        // Get the clip bounds to know what area to clear
        final java.awt.Rectangle bounds = g.getClipBounds();
        if (bounds != null) {
            g.setColor(DEFAULT_ROOM_COLOR);
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }

    /**
     * Draws the room-specific background and visual elements.
     * 
     * @param g the graphics context
     * @param room the room to draw background for
     */
    private void drawRoomBackground(final Graphics2D g, final Room room) {
        final java.awt.Rectangle bounds = g.getClipBounds();
        if (bounds == null) {
            return;
        }

        // Different background colors for different room types
        final Color roomColor = switch (room.getRoomType()) {
            case RoomGenerator.MAIN_ROOM -> MAIN_ROOM_COLOR;
            case RoomGenerator.PUZZLE_ROOM -> PUZZLE_ROOM_COLOR;
            default -> DEFAULT_ROOM_COLOR;
        };

        g.setColor(roomColor);
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

        // Draw room borders
        g.setColor(Color.WHITE);
        final int x = 5, y = 5;
        g.drawRect(x, y, bounds.width - 10, bounds.height - 10);

        // Draw room ID in corner
        g.setColor(Color.WHITE);
        final int offset = 25;
        g.drawString("Room " + room.getId(), 10, offset);
    }

    /**
     * Temporary accessor for integration with future GamePanel.
     *
     * @return the ScoreHud used for rendering the HUD
     */
    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP",
        justification = "Temporary accessor; remove when GamePanel calls renderHud directly"
    )
    public ScoreHud getScoreHud() {
        return scoreHud;
    }
}
