package main.attacks;

import main.GameType;
import main.character.Character;

/**
 * This abstract class represents an "attack" in the game.
 */
public abstract class Attack {
    protected int id;
    protected String name;
    protected String description;
    protected double atkValue;
    protected GameType type;

    public Attack (
            int id,
            String name,
            String type,
            String description,
            double atkValue
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.atkValue = atkValue;
        this.type = new GameType(type);
    }

    public abstract void dealDamage(Character self, Character target);

    public abstract int calcDamage();

    public String getName() {
        return name;
    }

}
