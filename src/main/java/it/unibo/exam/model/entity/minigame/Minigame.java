package it.unibo.exam.model.entity.minigame;

import javax.swing.JFrame;

/**
 * Base interface for all minigames in the system.
 * Each minigame must implement these methods to integrate with the game engine.
 */
public interface Minigame {

    /**
     * Starts the minigame with the specified parent frame and completion callback.
     * 
     * @param parentFrame the parent frame to center the minigame window
     * @param onComplete callback to invoke when the minigame is completed
     */
    void start(JFrame parentFrame, MinigameCallback onComplete);

    /**
     * Stops the minigame and closes any associated windows.
     * This method should clean up all resources used by the minigame.
     */
    void stop();

    /**
     * Gets the display name of this minigame.
     * 
     * @return the name of the minigame
     */
    String getName();

    /**
     * Gets a brief description of what the player needs to do in this minigame.
     * 
     * @return the description of the minigame
     */
    String getDescription();
}
