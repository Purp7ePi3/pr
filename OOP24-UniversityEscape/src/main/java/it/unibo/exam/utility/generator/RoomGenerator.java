package it.unibo.exam.utility.generator;

import it.unibo.exam.model.entity.enviroments.Room;
import it.unibo.exam.utility.geometry.Point2D;

/**
 * Room Generator.
 * @see Room
 */
public class RoomGenerator extends EntityGenerator<Room> {

    /**
     * RoomType:
     *  2 = PuzzleRoom.
     */
    public static final int PUZZLE_ROOM = 2;

    /**
     * RoomType:
     *  2 = PuzzleRoom.
     */
    public static final int MAIN_ROOM = 1;

    private final DoorGenerator dg;

    /**
     * @param enviromentSize
     */
    public RoomGenerator(final Point2D enviromentSize) {
        super(enviromentSize);
        this.dg = new DoorGenerator(enviromentSize);
    }

    /**
     * Generates a room with the specified ID.
     * 
     * @param id the ID of the room
     * @return the generated room
     */
    @Override
    public Room generate(final int id) {
        return new Room(
            id, 
            dg.generate(id), 
            id == 0 ? MAIN_ROOM : PUZZLE_ROOM
        );
    }
}
