package it.unibo.exam.model.game;

import java.util.List;
import java.util.stream.IntStream;
import java.util.logging.Logger;
import java.util.logging.Level;

import it.unibo.exam.model.entity.Player;
import it.unibo.exam.model.entity.Npc;
import it.unibo.exam.model.entity.enviroments.Room;
import it.unibo.exam.utility.generator.RoomGenerator;
import it.unibo.exam.utility.generator.NpcGenerator;
import it.unibo.exam.utility.geometry.Point2D;

/**
 * Represents the state of the game, including rooms, the player, and the current room.
 * Fixed version with proper resize handling.
 */
public class GameState {
    private static final Logger LOGGER = Logger.getLogger(GameState.class.getName());

    private final List<Room> rooms;
    private final Player player;
    private int currentRoomId;

    /**
     * Constructor for the GameState class.
     * Initializes the game state with a list of rooms and a player.
     *
     * @param enviromentSize the size of the environment
     */
    public GameState(final Point2D enviromentSize) {
        this.rooms = initRooms(enviromentSize);
        this.player = new Player(enviromentSize);
        this.currentRoomId = 0; // Main room ID

        // Initialize NPCs for puzzle rooms
        initializeNpcs(enviromentSize);
    }

    /**
     * Resizes the game elements to fit the new environment size.
     *
     * @param newSize the new size of the environment
     */
    public void resize(final Point2D newSize) {
        // Resize player
        getPlayer().resize(newSize);
        repositionPlayerWithinBounds(getPlayer(), newSize);

        // Update room generator for new door positions
        final RoomGenerator roomGenerator = new RoomGenerator(newSize);

        // Resize and reposition all doors in all rooms
        for (int i = 0; i < rooms.size(); i++) {
            final Room room = rooms.get(i);

            // Update doors with new positions
            roomGenerator.updateRoomDoors(i, room);

            // Resize NPC if present in puzzle rooms
            if (room.getRoomType() == RoomGenerator.PUZZLE_ROOM && room.getNpc() != null) {
                room.getNpc().resize(newSize);
                // Reposition NPC to maintain relative position
                repositionNpc(room.getNpc(), newSize, i);
            }
        }
    }

    /**
     * Repositions the player within the bounds of the new environment size.
     * @param player the player to reposition
     * @param environmentSize the new environment size
     */
    private void repositionPlayerWithinBounds(final Player player, final Point2D environmentSize) {
        final Point2D currentPos = player.getPosition();
        final Point2D playerSize = player.getDimension();
        // Calcola i limiti dell'ambiente (con margine)
        final int margin = 10;
        final int minX = margin;
        final int minY = margin;
        final int maxX = environmentSize.getX() - playerSize.getX() - margin;
        final int maxY = environmentSize.getY() - playerSize.getY() - margin;

        // Controlla se il player è fuori dai limiti
        int newX = currentPos.getX();
        int newY = currentPos.getY();
        boolean needsRepositioning = false;

        if (newX < minX) {
            newX = minX;
            needsRepositioning = true;
        } else if (newX > maxX) {
            newX = maxX;
            needsRepositioning = true;
        }

        if (newY < minY) {
            newY = minY;
            needsRepositioning = true;
        } else if (newY > maxY) {
            newY = maxY;
            needsRepositioning = true;
        }

        // Se il player è fuori dai limiti, riposizionalo
        if (needsRepositioning) {
            LOGGER.info(String.format(
                "Player out of bounds at (%d,%d), repositioning to (%d,%d)",
                currentPos.getX(),
                currentPos.getY(),
                newX,
                newY
            ));
            player.setPosition(newX, newY);
        }
    }

    /**
     * Repositions an NPC after resize to maintain relative position.
     * @param npc the NPC to reposition
     * @param newSize the new environment size
     * @param roomId the room ID
     */
    private void repositionNpc(final Npc npc, final Point2D newSize, final int roomId) {
        final int npcWidth = newSize.getX() / 20;
        final int npcHeight = newSize.getY() / 20;
        final int margin = 80;

        final Point2D newPosition;
        switch (roomId) {
            case 1: // Bar
                newPosition = new Point2D(margin, margin);
                break;
            case 2: // Lab
                newPosition = new Point2D(newSize.getX() / 2 - npcWidth / 2, newSize.getY() / 2 - npcHeight / 2);
                break;
            case 3: // Gym
                newPosition = new Point2D(newSize.getX() - npcWidth - margin, newSize.getY() - npcHeight - margin);
                break;
            case 4: // Garden
                newPosition = new Point2D(margin, newSize.getY() / 2 - npcHeight / 2);
                break;
            default:
                newPosition = new Point2D(newSize.getX() / 2 - npcWidth / 2, newSize.getY() / 2 - npcHeight / 2);
                break;
        }

        npc.setPosition(newPosition);
    }

    /**
     * Initializes the rooms for the game.
     *
     * @param enviromentSize the size of the environment
     * @return a list of rooms
     */
    private List<Room> initRooms(final Point2D enviromentSize) {
        final RoomGenerator rg = new RoomGenerator(enviromentSize);
        final int endExclusive = 5;
        return IntStream.range(0, endExclusive)
            .mapToObj(rg::generate)
            .toList();
    }

    /**
     * Initializes NPCs for all puzzle rooms.
     * 
     * @param environmentSize the size of the environment
     */
    private void initializeNpcs(final Point2D environmentSize) {
        final NpcGenerator npcGenerator = new NpcGenerator(environmentSize);
        final int npcWidth = environmentSize.getX() / 20;
        final int npcHeight = environmentSize.getY() / 20;

        for (int i = 1; i < rooms.size(); i++) {
            final Room room = rooms.get(i);
            if (room.getRoomType() == RoomGenerator.PUZZLE_ROOM) {
                try {
                    final Npc npc = npcGenerator.generate(i - 1);

                    // Posizioni personalizzate per ogni stanza
                    final Point2D npcPosition = calculateNpcPosition(i, environmentSize, npcWidth, npcHeight);

                    npc.setPosition(npcPosition);
                    room.attachNpc(npc);

                    LOGGER.info("NPC " + npc.getName() + " posizionato in room " + i 
                        + " alla posizione (" + npcPosition.getX() + "," + npcPosition.getY() + ")");

                } catch (final IllegalArgumentException e) {
                    LOGGER.log(Level.SEVERE, "Failed to generate NPC for room " + i + ": " + e.getMessage(), e);
                }
            }
        }
    }

    /**
     * Calculates custom position for NPC based on room ID.
     *
     * @param roomId identifier of room
     * @param environmentSize dimensions of environment
     * @param npcWidth NPC width
     * @param npcHeight NPC height
     * @return new position for NPC
     */
    private Point2D calculateNpcPosition(final int roomId, final Point2D environmentSize,
                                    final int npcWidth, final int npcHeight) {
        final int margin = 60;
        final int centerX = environmentSize.getX() / 2;
        final int centerY = environmentSize.getY() / 2;

        switch (roomId) {
            case 1:
                return new Point2D(margin, margin);
            case 2:
                return new Point2D(centerX - npcWidth / 2, centerY - npcHeight / 2);
            case 3:
                return new Point2D(environmentSize.getX() - npcWidth - margin,
                                    environmentSize.getY() - npcHeight - margin);
            case 4:
                return new Point2D(margin, centerY - npcHeight / 2);
            default:
                return new Point2D(centerX - npcWidth / 2, centerY - npcHeight / 2);
        }
    }

    /**
     * @return the current room
     */
    public Room getCurrentRoom() {
        return rooms.get(currentRoomId);
    }

    /**
     * Changes the current room to the specified room ID.
     *
     * @param newRoomId the ID of the new room
     */
    public void changeRoom(final int newRoomId) {
        if (newRoomId < 0 || newRoomId >= rooms.size()) {
            throw new IllegalArgumentException("Invalid room ID: " + newRoomId);
        }
        this.currentRoomId = newRoomId;
    }

    /**
     * @return the player instance
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return total number of rooms
     */
    public int getTotalRooms() {
        return rooms.size();
    }

    /**
     * @return the current room ID
     */
    public int getCurrentRoomId() {
        return currentRoomId;
    }

    /**
     * @return read-only list of all rooms
     */
    public List<Room> getAllRooms() {
        return List.copyOf(rooms);
    }
}
