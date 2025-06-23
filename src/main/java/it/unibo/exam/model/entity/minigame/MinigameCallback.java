package it.unibo.exam.model.entity.minigame;

/**
 * Callback interface invoked when a minigame is completed.
 * This allows the main game to react to minigame completion and update scores.
 */
public interface MinigameCallback {

    /**
     * Called when the minigame is completed, either successfully or unsuccessfully.
     * 
     * @param success true if the minigame was completed successfully, false otherwise
     * @param timeSeconds the time taken to complete the minigame in seconds
     */
    void onComplete(boolean success, int timeSeconds);
}
