package it.unibo.exam.model.entity;

import it.unibo.exam.utility.geometry.Point2D;

/**
 * Represents a non-playable character (NPC) in the game.
 * Updated with positioning methods.
 */
public final class Npc extends Entity {

    private static final Point2D DEFAULT_POSITION = new Point2D(0, 0);
    private final String name;
    private final String description;
    private final String dialogue;

    /**
     * Constructor for Npc.
     *
     * @param enviromentSize the size of the environment
     * @param name the name of the NPC
     * @param description the description of the NPC
     * @param dialogue the dialogue of the NPC
     */
    public Npc(final Point2D enviromentSize, final String name, 
               final String description, final String dialogue) {
        super(DEFAULT_POSITION, enviromentSize);
        this.name = name;
        this.description = description;
        this.dialogue = dialogue;
    }

    /**
     * @return the name of the NPC
     */
    public String getName() {
        return name;
    }

    /**
     * @return the description of the NPC
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the dialogue of the NPC
     */
    public String getDialogue() {
        return dialogue;
    }

    /**
     * Defines the interaction behavior with the NPC.
     */
    public void interact() {
        // Interaction logic can be implemented here
        System.out.println("Interacting with " + name + ": " + dialogue);
    }
    
    /**
     * Sets the position of the NPC.
     *
     * @param position the new position of the NPC
     */
    public void setPosition(final Point2D position) {
        this.getPosition().setXY(position.getX(), position.getY());
    }

    /**
     * Sets the position of the NPC.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public void setPosition(final int x, final int y) {
        this.getPosition().setXY(x, y);
    }

    /**
     * Sets the x-coordinate of the NPC's position.
     *
     * @param x the x coordinate
     */
    public void setPositionX(final int x) {
        this.getPosition().setXY(x, this.getPosition().getY());
    }

    /**
     * Sets the y-coordinate of the NPC's position.
     *
     * @param y the y coordinate
     */
    public void setPositionY(final int y) {
        this.getPosition().setXY(this.getPosition().getX(), y);
    }
}