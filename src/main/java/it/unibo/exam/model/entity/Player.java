package it.unibo.exam.model.entity;

import it.unibo.exam.model.data.RoomScoreData;
import it.unibo.exam.utility.geometry.Point2D;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the player in the game, including position and per-room scores.
 * Tracks progress, time taken, and points earned for each room.
 */
public class Player extends MovementEntity {

    /** Stores RoomScoreData for each room the player completes. */
    private final Map<Integer, RoomScoreData> roomScores = new HashMap<>();

    /**
     * Constructs a Player.
     *
     * @param enviromentSize the size of the environment
     */
    public Player(final Point2D enviromentSize) {
        super(enviromentSize);
    }

    /**
     * Records or updates the score data for a completed room.
     *
     * @param roomId    the ID of the room
     * @param timeTaken   time taken to complete the room (seconds or ms)
     * @param pointsGained points earned in the room
     */
    public void addRoomScore(final int roomId, final int timeTaken, final int pointsGained) {
        roomScores.put(roomId, new RoomScoreData(roomId, timeTaken, pointsGained, true));
    }

    /**
     * Returns a read-only map of all room scores.
     * The map key is the room's name.
     *
     * @return a map from room names to RoomScoreData
     */
    public Map<Integer, RoomScoreData> getRoomScores() {
        return Map.copyOf(roomScores);
    }

    /**
     * Returns the total score across all completed rooms.
     *
     * @return the sum of points for all completed rooms
     */
    public int getTotalScore() {
        return roomScores.values().stream()
                .mapToInt(RoomScoreData::getPointsGained)
                .sum();
    }

    /**
     * Checks if all rooms have been completed.
     *
     * @param numRooms total number of rooms in the game
     * @return true if all rooms are completed, false otherwise
     */
    public boolean allRoomsCompleted(final int numRooms) {
        return roomScores.size() == numRooms 
        && roomScores.values().stream().allMatch(RoomScoreData::isCompleted);
    }
}
