package main.character;

import java.util.*;

import main.Driver;
import main.attacks.*;
import main.items.Item;

// TODO revise everything
// TODO add docstring

public class NPC extends Character {

    private int id; 

    private String name;
    private int friendship;

    private String prompt;
    private String imagepath;

    ArrayList<String> possibleAttackDialogue = new ArrayList<>();

    private ArrayList<Attack> attacks;
    private ArrayList<Item> favoriteItems;
    private ArrayList<Item> hatedItems;

    /**
     * Indicates whether the NPC has been "selected" by the user or not.
     */
    public Boolean selected = false;

    public NPC (int id, String name, String type, String prompt, String image, int hp, int def) {
        super(hp, def, type);
        this.id = id;
        this.name = name;
        this.prompt = prompt;
        this.imagepath = image;
        friendship = 0;
        attacks = new ArrayList<Attack>();
        favoriteItems = new ArrayList<Item>();
        hatedItems = new ArrayList<Item>();
    }

    public String getName() {
        return this.name;
    }

    public int getId() {return this.id;}

    public String getImagePath() {
        return this.imagepath;
    }

    public int getFriendship() {
        return this.friendship;
    }

    public void addAttackDialogue(String dialogue) {
        possibleAttackDialogue.add(dialogue);
    }

    public ArrayList<String> getAttackDialogue() {
        return possibleAttackDialogue;
    }

    public void increaseFriendship(int amount) {
        friendship += amount;
        if (friendship > 100) {
            friendship = 100;
        }
        if (friendship < 0) {
            friendship = 0;
        }
    }

    public void onDefeated() {
        double reward = 5 + (Math.floor(this.maxHP / 10.0)) + (Math.floor(this.getDef() / 5.0));
        Driver.getPlayer().addCandies((int)reward);
        isAlive = false;
    }
}
