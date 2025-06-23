package it.unibo.exam.utility.generator;

import java.util.List;
import it.unibo.exam.model.entity.enviroments.Door;
import it.unibo.exam.model.entity.enviroments.Room;
import it.unibo.exam.utility.geometry.Point2D;

/**
 * Room Generator.
 * @see Room
 */
public class RoomGenerator extends EntityGenerator<Room> {

    /**
     * RoomType: 2 = PuzzleRoom.
     */
    public static final int PUZZLE_ROOM = 2;

    /**
     * RoomType: 1 = MainRoom.
     */
    public static final int MAIN_ROOM = 1;

    /**
     * Display names for each room, indexed by room ID.
     */
    private static final String[] ROOM_NAMES = {
        "2.12",
        "Bar",
        "Lab",
        "Gym",
        "Garden",
    };


    private final DoorGenerator dg;

    /**
     * Constructor.
     * @param enviromentSize the size of the environment
     */
    public RoomGenerator(final Point2D enviromentSize) {
        super(enviromentSize);
        this.dg = new DoorGenerator(enviromentSize);
    }

    /**
     * Updates the door generator when environment is resized.
     * @param newSize the new environment size
     */
    public void updateEnvironmentSize(final Point2D newSize) {
        this.dg.updateEnvironmentSize(newSize);
    }

    /**
     * Regenerates doors for a specific room with new environment size.
     * @param roomId the room ID
     * @param room the room to update
     */
    public void updateRoomDoors(final int roomId, final Room room) {
        final List<Door> newDoors = dg.generate(roomId);
        room.updateDoors(newDoors);
    }

    /**
     * Generates a room with the specified ID and assigns its display name.
     * @param id the ID of the room
     * @return the generated room
     */
    @Override
    public Room generate(final int id) {
        final Room room = new Room(
            id,
            dg.generate(id),
            id == 0 ? MAIN_ROOM : PUZZLE_ROOM
        );
        room.setName(ROOM_NAMES[id]);
        return room;
    }
}
