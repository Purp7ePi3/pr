package it.unibo.exam.model.game;

import java.util.List;
import java.util.stream.IntStream;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.exam.model.entity.Entity;
import it.unibo.exam.model.entity.Player;
import it.unibo.exam.model.entity.Npc;
import it.unibo.exam.model.entity.enviroments.Room;
import it.unibo.exam.utility.generator.RoomGenerator;
import it.unibo.exam.utility.generator.NpcGenerator;
import it.unibo.exam.utility.geometry.Point2D;

/**
 * Represents the state of the game, including rooms, the player, and the current room.
 * Updated to properly initialize NPCs in puzzle rooms.
 */
public class GameState {

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
        final List<Entity> entityList = List.of(getPlayer()); 
        final Room room = getCurrentRoom();
        entityList.addAll(room.getDoors());

        if (room.getRoomType() == RoomGenerator.PUZZLE_ROOM) { 
            entityList.add(room.getNpc());
        }

        entityList.forEach(e -> e.resize(newSize));
    }

    /**
     * Initializes the rooms for the game.
     *
     * @param enviromentSize the size of the environment
     * @return a list of rooms
     */
    private List<Room> initRooms(final Point2D enviromentSize) {
        final RoomGenerator rg = new RoomGenerator(enviromentSize);

        return IntStream.range(0, 5) // Changed to 5 to include rooms 0-4
            .mapToObj(rg::generate).toList();
    }
    
    /**
     * Initializes NPCs for all puzzle rooms.
     * 
     * @param environmentSize the size of the environment
     */
    private void initializeNpcs(final Point2D environmentSize) {
        final NpcGenerator npcGenerator = new NpcGenerator(environmentSize);
        
        // Add NPCs to puzzle rooms (rooms 1-4)
        for (int i = 1; i < rooms.size(); i++) {
            final Room room = rooms.get(i);
            if (room.getRoomType() == RoomGenerator.PUZZLE_ROOM) {
                try {
                    final Npc npc = npcGenerator.generate(i - 1); // Adjust index for NPC generator
                    
                    // Position NPC in the center-ish of the room
                    npc.setPositionX(environmentSize.getX() / 2 + (i * 50)); // Offset each NPC slightly
                    npc.setPositionY(environmentSize.getY() / 2 + (i * 30));
                    
                    room.attachNpc(npc);
                } catch (final Exception e) {
                    // Handle case where NPC generation fails
                    System.err.println("Failed to generate NPC for room " + i + ": " + e.getMessage());
                }
            }
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
    public void changeRoom(final int newRoomId) { // Made 'newRoomId' final
        // Check if the new room ID is valid
        if (newRoomId < 0 || newRoomId >= rooms.size()) {
            throw new IllegalArgumentException("Invalid room ID: " + newRoomId);
        }

        this.currentRoomId = newRoomId;
    }

    /**
     * @return the player instance
     */
    @SuppressFBWarnings(value = "EI", justification = "Player need to be updated")
    public Player getPlayer() {
        return player;
    }
    
    /**
     * Gets the total number of rooms in the game.
     * 
     * @return the number of rooms
     */
    public int getTotalRooms() {
        return rooms.size();
    }
    
    /**
     * Gets the current room ID.
     * 
     * @return the current room ID
     */
    public int getCurrentRoomId() {
        return currentRoomId;
    }
    
    /**
     * Gets a read-only list of all rooms.
     * 
     * @return list of all rooms
     */
    public List<Room> getAllRooms() {
        return List.copyOf(rooms);
    }
}