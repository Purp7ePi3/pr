package it.unibo.exam.model.entity.pokemon;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents a Pokemon with stats, moves, and battle capabilities.
 */
public class Pokemon {
    private final String name;
    private final int maxHp;
    private int currentHp;
    private final int attack;
    private final int defense;
    private final List<Move> moves;
    private final String description;
    private boolean fainted;
    
    /**
     * Creates a new Pokemon.
     * 
     * @param name the Pokemon's name
     * @param maxHp maximum hit points
     * @param attack attack stat
     * @param defense defense stat
     * @param moves list of available moves
     * @param description Pokemon description
     */
    public Pokemon(String name, int maxHp, int attack, int defense, List<Move> moves, String description) {
        this.name = name;
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.attack = attack;
        this.defense = defense;
        this.moves = new ArrayList<>(moves);
        this.description = description;
        this.fainted = false;
    }
    
    /**
     * Takes damage from an attack.
     * 
     * @param damage the amount of damage to take
     * @return the actual damage dealt after defense calculations
     */
    public int takeDamage(int damage) {
        // Calculate damage with defense (minimum 1 damage)
        int actualDamage = Math.max(1, damage - (defense / 3));
        
        currentHp = Math.max(0, currentHp - actualDamage);
        
        if (currentHp == 0 && !fainted) {
            fainted = true;
        }
        
        return actualDamage;
    }
    
    /**
     * Heals the Pokemon by the specified amount.
     * 
     * @param amount the amount of HP to restore
     */
    public void heal(int amount) {
        currentHp = Math.min(maxHp, currentHp + amount);
        if (currentHp > 0) {
            fainted = false;
        }
    }
    
    /**
     * Uses a move against a target Pokemon.
     * 
     * @param move the move to use
     * @param target the target Pokemon
     * @return damage dealt
     */
    public int useMove(Move move, Pokemon target) {
        if (fainted) {
            return 0;
        }
        
        // Calculate final damage with attack stat
        int finalDamage = move.getDamage() + (attack / 4);
        
        // Add some randomness (90-110% damage)
        double randomMultiplier = 0.9 + (Math.random() * 0.2);
        finalDamage = (int) (finalDamage * randomMultiplier);
        
        return target.takeDamage(finalDamage);
    }
    
    /**
     * Gets the HP percentage for display.
     * 
     * @return HP percentage (0-100)
     */
    public int getHpPercentage() {
        return (int) ((double) currentHp / maxHp * 100);
    }
    
    /**
     * Gets HP status color based on current HP.
     * 
     * @return color name for HP bar
     */
    public String getHpColor() {
        int percentage = getHpPercentage();
        if (percentage > 50) return "green";
        if (percentage > 20) return "yellow";
        return "red";
    }
    
    // Getters
    public String getName() { return name; }
    public int getMaxHp() { return maxHp; }
    public int getCurrentHp() { return currentHp; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public List<Move> getMoves() { return new ArrayList<>(moves); }
    public String getDescription() { return description; }
    public boolean isFainted() { return fainted; }
    
    @Override
    public String toString() {
        return name + " (" + currentHp + "/" + maxHp + " HP)";
    }
}