package it.unibo.exam.model.entity.enviroments;

import java.util.ArrayList;
import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.exam.model.entity.Npc;
import it.unibo.exam.model.entity.minigame.Minigame;
import it.unibo.exam.utility.generator.RoomGenerator;

/**
 * A simple Room class rappresenting a room.
 */
public class Room {

    private final int id;
    private Minigame minigame;
    private final int roomType;
    private Npc npc;
    private final List<Door> doors;


    /**
     * Contractor.
     * @param id the id of the room
     * @param doors the doors of the room
     * @param roomType the type of the room
     */
    public Room(final int id, final List<Door> doors, final int roomType) {
        this.id = id;
        this.doors = new ArrayList<>(doors);
        this.roomType = roomType;
    }

    /**
     * @return the id of the room
     */
    public int getId() {
        return id;
    }

    /**
     * @return the minigame of the room
     * @throws IllegalStateException if the room has no minigame
     */
    public Minigame getMinigame() {
        if (roomType == 1) {
            throw new IllegalStateException("This room has no minigame");
        }
        return minigame;
    }

    /**
     * @return the doors of the room
     */
    @SuppressFBWarnings(value = "EI", justification = "Safe final list")
    public List<Door> getDoors() {
        return new ArrayList<>(doors);
    }

    /**
     * @return the type of the room
     */
    public int getRoomType() {
        return roomType;
    }

    /**
     * @return the npc of the room
     * @throws IllegalStateException if the room has no npc
     */
    public Npc getNpc() {
        if (roomType == 1) {
            throw new IllegalStateException("This room has no npc");
        }
        return npc;
    }

    /**
     * @param npc NPC
     */
    public void attachNpc(final Npc npc) {
        if (roomType == RoomGenerator.MAIN_ROOM) {
            throw new IllegalStateException("Main room has no npc");
        }
        this.npc = npc;
    }

    /**
     * @param mg minigame
     */
    public void attacMinigame(final Minigame mg) {
        if (roomType == RoomGenerator.MAIN_ROOM) {
            throw new IllegalStateException("Main room has no minigame");
        }
        this.minigame = mg;
    }

}
