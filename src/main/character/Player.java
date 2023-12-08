package main.character;

import java.util.*;

import main.*;

import javafx.scene.input.KeyCode;
import main.attacks.Attack;
import main.attacks.AttackStandard;
import main.items.Costume;
import main.items.Item;

/**
 * Represents a player.
 */
public class Player extends Character {

    /**
     * Represents the current location of the player (referenced by id).
     */
    private int location;

    /**
     * Represents the currently-equipped costume (referenced by id).
     */
    private Costume equipped;

    /**
     * Represents the current kill count of the player.
     */
    private int killCount;

    /**
     * Represents the current friend score of the player.
     */
    private int friendScore;

    /**
     * Represents the current amount of candies collected by the player.
     */
    private int candiesCollected;

    /**
    * Represents the collection of costumes in the player's possession.
    */
    public ArrayList<Costume> wardrobe;

    /**
    * Represents the collection of items in the player's possession.
    */
    public ArrayList<Item> inventory;

    /**
    * Tracks the neighbourhoods (referenced by id) explored by the player.
    */
    public Set<Integer> neighbourhoodsExplored;

    /**
     * Constructor for Player.
     * Initializes default values for the player.
     */
    public Player() {
        super(20, 10, "");
        this.location = 0;
        this.killCount = 0;
        this.friendScore = 0;
        this.candiesCollected = 0;
        this.wardrobe = new ArrayList<>();
        this.inventory = new ArrayList<>();
        this.neighbourhoodsExplored = new HashSet<>();
        this.neighbourhoodsExplored.add(this.location);

        this.attacks.add(new AttackStandard(-1, "Poke", "FIEND", "Poke attack.", 5));
    }

    /**
     * Retrieves the current location.
     *
     * @return The integer representing current location.
     */
    public int getLocation() {
        return this.location;
    }

    /**
     * Retrieves the kill count.
     *
     * @return The integer representing kill count.
     */
    public int getKillCount(){
        return this.killCount;
    }

    /**
     * Retrieves the friend score.
     *
     * @return The integer representing friend score.
     */
    public int getFriendScore(){
        return this.friendScore;
    }

    /**
     * Retrieves the currently-equipped costume of the player.
     *
     * @return The currently-equipped Costume object.
     */
    public Costume getEquippedCostume() {
        return equipped;
    }

    /*
     * Updates player location.
     *
     * @param kc The KeyCode representing the desired direction of movement.
     * @return The boolean representing whether or not the player location was successfully indicated.
     */
    public boolean updateLocation(KeyCode kc){
        int newLocation = Driver.getMap().get(location).takePath(kc);
        if (newLocation == -1) {
            return false;
        } else {
            this.location = newLocation;
            neighbourhoodsExplored.add(this.location);
            return true;
        }
    }

    /**
     * Equips a different costume.
     *
     * @param costume The index (in wardrobe) of the costume to be equipped.
     */
    public void equipCostume(Costume costume){
        equipped = costume;

        Driver.getPlayer().getAttacks().clear();
        for (Attack attack : costume.getAttacks()) {
            Driver.getPlayer().addAttack(attack);
        }
        updateStats(this.getEquippedCostume());
    }

    /**
     * Updates the player statistics based on the equipped costume.
     *
     * @param costume The currently-equipped Costume object used to update player statistics.
     */
    public void updateStats(Costume costume){
        this.type = costume.getType();

        double ratio = (double)this.HP / (double)this.maxHP;
        this.maxHP = (int) (this.maxHP * costume.getHPMultiplier());
        this.HP = (int)Math.ceil(this.maxHP * ratio);

        this.def = (int)Math.ceil(10 * costume.getDefenseMultiplier());
    }

    // TODO revise
    /**
     * Updates the kill count.
     *
     * @param count The new kill count.
     */
    public void updateKillCount(int count){
        this.killCount = count;
    }

    // TODO revise
    /**
     * Updates the friend score.
     *
     * @param count The new friend score.
     */
    public void updateFriendScore(int count){
        this.friendScore = count;
    }

    /**
     * Updates the candies collected. 
     * every kill gives the player 8 candies and every friend gives the player 10 candies
     */
    public void updateCandiesCollected(){
        candiesCollected = killCount * 8 + friendScore * 10;
    }

    /**
     * Manually add/remove candies from the player.
     *
     * @param count the number of candies to add or remove (if negative)
     */
    public void addCandies(int count) {
        candiesCollected += count;
    }

    /**
     * Retrieves the current amount of candies collected.
     *
     * @return amount of candies collected.
     */
    public int getCandiesCollected(){
        return candiesCollected;
    }

    /**
     * Adds a new costume to the player's wardrobe.
     *
     * @param newCostume The new Costume object to be added to the wardrobe.
     */
    public void addCostume(Costume newCostume) {
        wardrobe.add(newCostume);
    }

    /**
     * Adds a new item to the player's inventory.
     *
     * @param newItem The new Item object to be added to the inventory.
     */
    public void takeItem(Item newItem) {
        inventory.add(newItem);
    }

    /**
     * Removes an item from the player's inventory.
     *
     * @param idx The index of the item to be removed from the inventory.
     * @return The removed Item object.
     */
    public Item dropItem(int idx) {
        Item dropped = inventory.get(idx);
        inventory.remove(idx);
        return dropped;
    }

    /**
     * Retrieves player statistics.
     *
     * @return A HashMap containing player statistics.
     */
    public HashMap<String, String> getStatistics(){
        HashMap<String, String> stats =  new HashMap<String, String>();
        Integer killCount = getKillCount(), friendScore = getFriendScore(), wardrobeSize = wardrobe.size(), explored = neighbourhoodsExplored.size(), candies = getCandiesCollected();
        stats.put("Kill count", killCount.toString());
        stats.put("Friend Score", friendScore.toString());
        stats.put("Costumes Collected", wardrobeSize.toString());
        stats.put("Candies Collected", candies.toString());
        stats.put("Neighbourhoods explored", explored.toString());
        return stats;
    }

    /**
     * Retrieves costume information (for the currently-equipped costume).
     *
     * @return The String containing information about the currently-equipped costume.
     */
    public String getCostumeInfo(){
        Costume costume = getEquippedCostume();
        return "Costume: " + costume.getName() + "\nCostume description: " + costume.getDescription() +
         "\nCostume type: " + costume.getType() + "\nCostume attacks: " + costume.getAttacks();
    }

}
