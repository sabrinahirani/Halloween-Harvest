package main.items;

import java.util.*;

import main.*;
import main.attacks.*;

/**
 * Represents a costume.
 */
public class Costume implements Item {

    /**
     * The id of the costume.
     */
    private final int id;

    /**
     * The name of the costume.
     */
    private final String name;

    /*
     * The type of the costume.
     */
    private final GameType type;

    /**
     * The description of the costume.
     */
    private final String description;

    /**
     * The filename of the icon of the costume.
     */
    private final String icon;

    /**
     * The hp multiplier of the costume.
     */
    private final double HPMultiplier;

    /**
     * The damage multiplier of the costume.
     */
    private final double damageMultiplier;

    /**
     * The defense multiplier of the costume.
     */
    private final int defenseMultiplier;

    /**
     * The attacks associated with the costume.
     */
    private final ArrayList<Attack> attacks;

    /**
     * Constructor for the Costume class.
     *
     * @param id          The id of the costume.
     * @param name        The name of the costume.
     * @param type        The type of the costume.
     * @param description The description of the costume.
     * @param icon        The filename of the icon representing the costume.
     * @param hp          The HP multiplier of the costume.
     * @param damage      The damage multiplier of the costume.
     * @param defense     The defense multiplier of the costume.
     */
    public Costume(int id, String name, String type, String description, String icon, double hp, double damage, int defense) {
        this.id = id;
        this.name = name;
        this.type = new GameType(type);
        this.description = description;
        this.icon = icon;
        this.HPMultiplier = hp;
        this.damageMultiplier = damage;
        this.defenseMultiplier = defense;
        this.attacks = new ArrayList<>();
    }

    /**
     * Retrieves the id of the costume.
     *
     * @return The integer representing the name of the costume.
     */
    public int getId(){
        return this.id;
    }

    /**
     * Retrieves the name of the costume.
     *
     * @return The String representing the name of the costume.
     */
    public String getName(){
        return this.name;
    }

    /**
     * Retrieves the GameType of the costume.
     *
     * @return The GameType representing the type of the costume.
     */
    public GameType getType(){
        return this.type;
    }

    /**
     * Retrieves the description of the costume.
     *
     * @return The String representing the description of the costume.
     */
    public String getDescription(){
        return this.description;
    }

    /**
     * Retrieves the filename of the icon of the costume.
     *
     * @return The String representing the filename of the icon of the costume.
     */
    public String getIcon(){
        return this.icon;
    }

    /**
     * Retrieves the HP multiplier of the costume.
     *
     * @return The double representing the HP multiplier of the costume
     */
    public double getHPMultiplier(){
        return this.HPMultiplier;
    }

    /**
     * Retrieves the damage multiplier of the costume.
     *
     * @return The double representing the damage multiplier of the costume.
     */
    public double getDamageMultiplier(){
        return this.damageMultiplier;
    }

    /**
     * Retrieves the defense multiplier of the costume.
     *
     * @return The double representing the defense multiplier of the costume.
     */
    public double getDefenseMultiplier(){
        return this.defenseMultiplier;
    }

    /*
     * Adds an attack to the costume.
     * 
     * @param newAttack The Attack to be added.
     */
    public void addAttack(Attack newAttack) {
        attacks.add(newAttack);
    }

    /**
     * Retrieves all attacks associated with the costume.
     *
     * @return ArrayList<Attack> The list of attacks.
     */
    public ArrayList<Attack> getAttacks(){
        return this.attacks;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Costume otherCostume = (Costume) other;
        return this.id == otherCostume.getId();
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public int getPrice() {
        // TODO: price of costumes? should be high?
        return 0;
    }

    @Override
    public boolean getPerishable() {
        return false;
    }

    @Override
    public void apply() {
        Driver.getPlayer().equipCostume(this);
    }
}
