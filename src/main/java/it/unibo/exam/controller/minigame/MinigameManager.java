package it.unibo.exam.controller.minigame;

import it.unibo.exam.model.entity.minigame.Minigame;
import it.unibo.exam.model.entity.minigame.MinigameCallback;
import it.unibo.exam.utility.factory.MinigameFactory;
import it.unibo.exam.controller.MainController;

import javax.swing.JFrame;
import java.util.logging.Logger;
import java.util.Objects;
import java.util.logging.Level;

/**
 * Manages the execution and completion of minigames.
 * Acts as a bridge between the main game and individual minigames.
 */
public class MinigameManager {

    private static final Logger LOGGER = Logger.getLogger(MinigameManager.class.getName());

    private final MainController mainController;
    private final JFrame parentFrame;
    private Minigame currentMinigame;

    /**
     * Creates a new MinigameManager.
     *
     * @param mainController the main game controller
     * @param parentFrame the parent frame for centering minigame windows
     */
    public MinigameManager(final MainController mainController, final JFrame parentFrame) {
        this.mainController = Objects.requireNonNull(mainController);
        this.parentFrame = Objects.requireNonNull(parentFrame);
    }

    /**
     * Starts a minigame for the specified room.
     *
     * @param roomId the ID of the room (determines which minigame to start)
     */
    public void startMinigame(final int roomId) {
        try {
            // Stop any currently running minigame
            stopCurrentMinigame();

            // Create the appropriate minigame for this room
            currentMinigame = MinigameFactory.createMinigame(roomId);

            LOGGER.info("Starting minigame: " + currentMinigame.getName() + " for room " + roomId);

            // Start the minigame with completion callback
            currentMinigame.start(parentFrame, new MinigameCallback() {
                @Override
                public void onComplete(final boolean success, final int timeSeconds) {
                    handleMinigameComplete(roomId, success, timeSeconds);
                }
            });

            // Notify the main controller that a minigame has started
            mainController.startMinigame(roomId);

        } catch (final IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "Invalid room ID for minigame: " + roomId, e);
        } catch (final SecurityException e) {
            LOGGER.log(Level.SEVERE, "Security error creating minigame window for room " + roomId, e);
        } catch (final IllegalStateException e) {
            LOGGER.log(Level.SEVERE, "Invalid state when starting minigame for room " + roomId, e);
        }
    }

    /**
     * Handles the completion of a minigame.
     *
     * @param roomId the ID of the room
     * @param success whether the minigame was completed successfully
     * @param timeSeconds the time taken to complete the minigame
     */
    private void handleMinigameComplete(final int roomId, final boolean success, final int timeSeconds) {
        LOGGER.info("Minigame completed for room " + roomId
                   + ". Success: " + success + ", Time: " + timeSeconds + "s");

        // Notify the main controller of completion
        mainController.endMinigame(success);

        // Clear the current minigame reference
        currentMinigame = null;

        // Show completion feedback (optional)
        showCompletionFeedback(roomId, success, timeSeconds);
    }

    /**
     * Stops the currently running minigame if one exists.
     */
    public void stopCurrentMinigame() {
        if (currentMinigame != null) {
            LOGGER.info("Stopping current minigame: " + currentMinigame.getName());
            currentMinigame.stop();
            currentMinigame = null;
        }
    }

    /**
     * Checks if a minigame is currently running.
     *
     * @return true if a minigame is currently active
     */
    public boolean isMinigameRunning() {
        return currentMinigame != null;
    }

    /**
     * Gets the currently running minigame.
     *
     * @return the current minigame or null if none is running
     */
    public Minigame getCurrentMinigame() {
        return currentMinigame;
    }

    /**
     * Shows feedback to the player after completing a minigame.
     *
     * @param roomId the room ID
     * @param success whether the minigame was successful
     * @param timeSeconds the time taken
     */
    private void showCompletionFeedback(final int roomId, final boolean success, final int timeSeconds) {
        final String minigameName = MinigameFactory.getMinigameName(roomId);
        final String message;

        if (success) {
            message = String.format("Congratulations! You completed '%s' in %d seconds!",
                                   minigameName, timeSeconds);
        } else {
            message = String.format("Minigame '%s' not completed. Try again!", minigameName);
        }

        LOGGER.info("Feedback: " + message);

    }
}
