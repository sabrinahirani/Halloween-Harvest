package main.character;

import main.GameType;
import main.attacks.*;

import java.util.ArrayList;

public class Character {
    protected int maxHP;
    protected int HP;
    protected int def;
    protected GameType type;
    protected boolean isAlive = true;
    protected String currStatusEffects = ""; // TODO: Array of Status effect classes.
    protected ArrayList<Attack> attacks = new ArrayList<>();

    Character(int HP, int def, String type) {
        this.HP = HP;
        this.maxHP = HP;
        this.def = def;
        this.type = new GameType(type);
    }

    public void damageHP(int damage) {
        HP -= damage;
        if (HP > maxHP) { HP = maxHP; }
    }

    public void addAttack(Attack attack) {
        attacks.add(attack);
    }

    public void clearAttackList() {
        attacks.clear();
    }

    public void pronounceDead(){
        isAlive = false;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public int getCurrentHP() {
        return HP;
    }

    public int getDef() {
        return def;
    }

    public GameType getType() {
        return type;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public ArrayList<Attack> getAttacks() {
        return attacks;
    }

    /**
     * Add health points to the player, for effects like healing.
     */
    public void addHp(int pts) {
        int newHp = HP + pts;
        if (newHp < 0) {
            HP = 0;
        } else if (newHp > 100) {
            HP = 100;
        } else {
            HP = newHp;
        }
    }
}
