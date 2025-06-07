package it.unibo.exam.view.hud;

import it.unibo.exam.model.game.GameState;
import it.unibo.exam.model.entity.Player;
import it.unibo.exam.model.data.RoomScoreData;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Map;

/**
 * Renders a non-interactive HUD showing per-room progress and scores.
 */
public class ScoreHud {

    private static final int START_X = 650;
    private static final int START_Y = 30;
    private static final int LINE_HEIGHT = 24;
    private static final int PADDING = 10;
    private static final int PANEL_WIDTH = 240;
    private static final int ARC_RADIUS = 15;
    private static final Color BG_COLOR = new Color(0, 0, 0, 150);
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font TEXT_FONT = new Font("Arial", Font.PLAIN, 14);

    private final GameState gameState;

    /**
     * Constructs the Score HUD.
     *
     * @param gameState the current game state, providing player and room data
     */
    public ScoreHud(final GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Draws the score HUD in the upper right corner of the screen.
     *
     * @param g the graphics context to draw on
     */
    public void draw(final Graphics2D g) {
        final Player player = gameState.getPlayer();
        final Map<Integer, RoomScoreData> scores = player.getRoomScores();

        // Calculate panel height based on number of rooms + title + total line
        final int panelHeight = (scores.size() + 2) * LINE_HEIGHT + PADDING * 2;

        // Draw background
        g.setColor(BG_COLOR);
        g.fillRoundRect(START_X - PADDING,
                        START_Y - LINE_HEIGHT,
                        PANEL_WIDTH,
                        panelHeight,
                        ARC_RADIUS,
                        ARC_RADIUS);

        // Draw title
        g.setFont(TITLE_FONT);
        g.setColor(Color.WHITE);
        g.drawString("Progress", START_X, START_Y);

        // Draw each room's data
        g.setFont(TEXT_FONT);
        int y = START_Y + LINE_HEIGHT;
        for (final RoomScoreData data : scores.values()) {
            final String check = data.isCompleted() ? "✓" : "[ ]";
            final String time = data.isCompleted()
                                ? data.getTimeTaken() + "s"
                                : "--";
            final String pts  = data.isCompleted()
                                ? data.getPointsGained() + " pts"
                                : "--";
            final String line = String.format("%s %s | %s | %s",
                                              check,
                                              data.getRoomName(),
                                              time,
                                              pts);
            g.drawString(line, START_X, y);
            y += LINE_HEIGHT;
        }

        // Draw total score
        g.setFont(TITLE_FONT);
        final String total = "Total: " + player.getTotalScore() + " pts";
        g.drawString(total, START_X, y + (LINE_HEIGHT / 2));
    }
}
