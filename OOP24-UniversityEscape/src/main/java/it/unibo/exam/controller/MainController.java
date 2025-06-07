package it.unibo.exam.controller;

import it.unibo.exam.controller.input.KeyHandler;
import it.unibo.exam.controller.position.PlayerPositionManager;
import it.unibo.exam.model.entity.Player;
import it.unibo.exam.model.entity.enviroments.Room;
import it.unibo.exam.model.entity.enviroments.Door;
import it.unibo.exam.model.game.GameState;
import it.unibo.exam.utility.geometry.Point2D;
import it.unibo.exam.view.GameRenderer;

/**
 * Main controller of the game.
 * It handles the game loop and the main logic of the game.
 * It is responsible for updating the game state of the game.
 * Fixed version with proper method signatures.
 */
public class MainController {

    private static final int FPS = 60;
    private static final double SECOND = 1_000_000_000.0;

    private static final int FAST_THRESHOLD = 30;
    private static final int MEDIUM_THRESHOLD = 60;
    private static final int POINTS_FAST = 100;
    private static final int POINTS_MEDIUM = 70;
    private static final int POINTS_SLOW = 40;

    private final KeyHandler keyHandler;
    private final GameState gameState;
    private final GameRenderer gameRenderer;
    private boolean running;
    private Point2D environmentSize;

    private long minigameStartTime;
    private boolean minigameActive;
    private int currentMinigameRoomId = -1;
    
    // Track last used door for positioning
    private Door lastUsedDoor = null;

    /**
     * @param enviromentSize size of the Game panel
     */
    public MainController(final Point2D enviromentSize) {
        this.keyHandler = new KeyHandler();
        this.gameState = new GameState(enviromentSize);
        this.gameRenderer = new GameRenderer(gameState);
        this.environmentSize = new Point2D(enviromentSize);
    }

    /**
     * Resize all the entity.
     * @param newSize new size of the Game Panel
     */
    public void resize(final Point2D newSize) {
        this.environmentSize = new Point2D(newSize);
        this.gameState.resize(newSize);
    }

    /**
     * Starts the game loop.
     */
    public void start() {
        running = true;
        gameLoop();
    }

    /**
     * Stops the game loop.
     */
    public void stop() {
        running = false;
    }

    /**
     * Gets the game renderer for external rendering calls.
     * 
     * @return the game renderer
     */
    public GameRenderer getGameRenderer() {
        return gameRenderer;
    }

    /**
     * Gets the key handler for external input management.
     * 
     * @return the key handler
     */
    public KeyHandler getKeyHandler() {
        return keyHandler;
    }

    /**
     * Main game loop.
     */
    private void gameLoop() {
        long lastTime = System.nanoTime();
        final long nsPerUpdate = (long) (SECOND / FPS);
        long accumulatedTime = 0;

        while (running) {
            final long now = System.nanoTime();
            accumulatedTime += now - lastTime;
            lastTime = now;

            while (accumulatedTime >= nsPerUpdate) {
                update();
                accumulatedTime -= nsPerUpdate;
            }

            // Rendering is now handled externally by GamePanel
            // The render() method is removed as it's no longer needed

            try {
                Thread.sleep(1);
            } catch (final InterruptedException e) {
                e.printStackTrace(); //NOPMD AvoidPrintStackTrace
            }
        }
    }

    /**
     * Updates the game state.
     */
    private void update() {
        final Player player = gameState.getPlayer();
        final Room room = gameState.getCurrentRoom();

        movePlayer(player);
        checkInteraction(player, room);
        checkWin();
    }

    /**
     * Check win condition.
     */
    private void checkWin() {
        // Implementation for win condition check
        // Can be expanded based on game requirements
    }

    /**
     * Check interaction with doors and npc's.
     * @param player Player
     * @param room Current Room
     */
    private void checkInteraction(final Player player, final Room room) {
        // Check for collisions with doors
        room.getDoors().forEach(door -> {
            if (player.getHitbox().intersects(door.getHitbox()) && keyHandler.isInteractPressed()) {
                // Store the door used for transition
                lastUsedDoor = door;
                
                // Change room
                gameState.changeRoom(door.getToId());
                
                // Position player appropriately in the new room
                positionPlayerAfterRoomChange(door);
            }
        });

        // Check for collisions with NPCs
        if (room.getNpc() != null
        && player.getHitbox().intersects(room.getNpc().getHitbox())
        && keyHandler.isInteractPressed()) {
            // Interact with NPC
            room.getNpc().interact();

            // Simulation starting and ending a minigame
            // Replace with minigame integration later
            final int roomId = gameState.getCurrentRoom().getId();
            startMinigame(roomId);
            // Simulate instant completion for testing:
            endMinigame(true);
            // ==========================================================
        }
    }

    /**
     * Positions the player after changing rooms based on the door used.
     * 
     * @param usedDoor the door that was used to transition
     */
    private void positionPlayerAfterRoomChange(final Door usedDoor) {
        final Player player = gameState.getPlayer();
        final Room newRoom = gameState.getCurrentRoom();
        
        // Find the corresponding door in the new room (door leading back)
        Door correspondingDoor = null;
        for (Door door : newRoom.getDoors()) {
            if (door.getToId() == usedDoor.getFromId()) {
                correspondingDoor = door;
                break;
            }
        }
        
        if (correspondingDoor != null) {
            // Position player near the corresponding door but inside the room
            PlayerPositionManager.positionPlayerAfterTransition(player, correspondingDoor, environmentSize);
        } else {
            // Fallback to center positioning
            player.setPosition(PlayerPositionManager.getDefaultSpawnPosition(environmentSize));
        }
    }

    /**
     * Moves the player based on input.
     * This method should be called every frame to update the player's position.
     * It checks for input from the user and updates the player's position accordingly.
     * It also checks for collisions with walls and other entities.
     * @param player 
     */
    private void movePlayer(final Player player) {
        final int speed = player.getSpeed();

        if (keyHandler.isUpPressed()) {
            player.move(0, -speed);
        }
        if (keyHandler.isDownPressed()) {
            player.move(0, speed);
        }
        if (keyHandler.isLeftPressed()) {
            player.move(-speed, 0);
        }
        if (keyHandler.isRightPressed()) {
            player.move(speed, 0);
        }
    }

    // Point system methods

    /**
     * Starts timing for a minigame in the specified room.
     * @param roomId the room ID for the minigame
     */
    public void startMinigame(final int roomId) {
        minigameStartTime = System.currentTimeMillis();
        minigameActive = true;
        currentMinigameRoomId = roomId;
    }

    /**
     * Ends the current minigame and updates the player's score if successful.
     * @param success true if the minigame was completed successfully
     */
    public void endMinigame(final boolean success) {
        if (minigameActive && currentMinigameRoomId >= 0 && success) {
            final int timeTaken = (int) ((System.currentTimeMillis() - minigameStartTime) / 1000);
            final int pointsGained = calculatePoints(timeTaken);
            gameState.getPlayer().addRoomScore(currentMinigameRoomId, timeTaken, pointsGained);
        }
        minigameActive = false;
        currentMinigameRoomId = -1;
    }

    /**
     * Calculates points for a minigame in a room based on time taken.
     * @param timeTaken time taken to complete (in seconds)
     * @return the number of points to award
     */
    private int calculatePoints(final int timeTaken) {
        if (timeTaken < FAST_THRESHOLD) {
            return POINTS_FAST;
        }
        if (timeTaken < MEDIUM_THRESHOLD) {
            return POINTS_MEDIUM;
        }
        return POINTS_SLOW;
    }
}