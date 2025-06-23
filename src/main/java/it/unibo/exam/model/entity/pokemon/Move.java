package it.unibo.exam.model.entity.pokemon;

/**
 * Represents a move that can be used in Pokemon battle.
 * Each move has a name, damage, and description.
 */
public class Move {
    private final String name;
    private final int damage;
    private final String description;
    private final MoveType type;
    
    /**
     * Types of moves for different effects.
     */
    public enum MoveType {
        PHYSICAL,    // Direct damage
        SPECIAL,     // Special effects + damage
        STATUS       // Status effects, usually no damage
    }
    
    /**
     * Creates a new move.
     * 
     * @param name the name of the move
     * @param damage the damage dealt by the move
     * @param description the description of the move
     * @param type the type of move
     */
    public Move(String name, int damage, String description, MoveType type) {
        this.name = name;
        this.damage = damage;
        this.description = description;
        this.type = type;
    }
    
    /**
     * Creates a simple physical move.
     * 
     * @param name the name of the move
     * @param damage the damage dealt
     */
    public Move(String name, int damage) {
        this(name, damage, name + " attack", MoveType.PHYSICAL);
    }
    
    public String getName() { return name; }
    public int getDamage() { return damage; }
    public String getDescription() { return description; }
    public MoveType getType() { return type; }
    
    @Override
    public String toString() {
        return name + " (" + damage + " damage)";
    }
}

