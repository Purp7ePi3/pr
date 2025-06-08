package it.unibo.exam.model.data;

/**
 * Data class for storing the player's score and completion state for a room.
 */
public class RoomScoreData {
    // Room names mapped by room ID
    private static final String[] ROOM_NAMES = {
        "2.12",       // 0
        "Bar",   // 1
        "Lab",       // 2
        "Gym",   // 3
        "Garden",   // 4
    };

    private final int roomId;
    private final int timeTaken;
    private final int pointsGained;
    private final boolean completed;

    /**
     * Constructs a RoomScoreData object with the given details.
     *
     * @param roomId        the ID of the room
     * @param timeTaken     time taken to complete the minigame (seconds or ms)
     * @param pointsGained  points gained for the room
     * @param completed     true if the room has been completed, false otherwise
     */
    public RoomScoreData(final int roomId, final int timeTaken, final int pointsGained, final boolean completed) {
        this.roomId = roomId;
        this.timeTaken = timeTaken;
        this.pointsGained = pointsGained;
        this.completed = completed;
    }

    /**
     * Gets the name of the room based on its ID.
     *
     * @return the room's name
     */
    public String getRoomName() {
        if (roomId >= 0 && roomId < ROOM_NAMES.length) {
            return ROOM_NAMES[roomId];
        }
        return "Unknown";
    }

    /**
     * Gets the time taken to complete the room's minigame.
     *
     * @return the time taken (in seconds or milliseconds, depending on usage)
     */
    public int getTimeTaken() {
        return timeTaken;
    }

    /**
     * Gets the number of points gained for completing the room.
     *
     * @return the points gained
     */
    public int getPointsGained() {
        return pointsGained;
    }

    /**
     * Indicates whether the room has been completed.
     *
     * @return true if the room has been completed, false otherwise
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Returns a string representation of this RoomScoreData.
     *
     * @return a string summarizing the room score data
     */
    @Override
    public String toString() {
        return "RoomScoreData{"
            + "roomId=" + roomId
            + ", roomName='" + getRoomName() + '\''
            + ", timeTaken=" + timeTaken
            + ", pointsGained=" + pointsGained
            + ", completed=" + completed
            + '}';
    }

}
