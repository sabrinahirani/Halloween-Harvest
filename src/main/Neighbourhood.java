package main;

import java.util.*;

import main.character.NPC;

import javafx.scene.input.KeyCode;
import main.items.Costume;
import main.items.Item;

/**
 * The Neighbourhood class represents a specific neighbourhood within the game map,
 * storing information including id, neighbourhood name, path to background image, 
 * paths to adjacent neighbourhoods, costumes situated in the neigbourhood,
 * and non-playable characters (NPCs) situated in the neighbourhood.
 */
public class Neighbourhood {

    /**
    * The id of the neighbourhood.
    */
    private final int id;

    /**
    * The name of the neighbourhood.
    */
    private final String name;

    /**
    * The filename of the background image of the neighbourhood.
    */
    private final String background;

    /**
    * Maps directions to destinations for player movement representing paths from the neighbourhood.
    */
    private final HashMap<KeyCode, Integer> path;

    /**
    * Stores items situated in the neighbourhood.
    */
    private ArrayList<Item> items;

    /**
    * Stores npcs situated in the neighbourhood.
    */
    private NPC npc;

    /**
     * Constructs a Neighbourhood with the specified id, name, and background.
     *
     * @param id         The id of the neighbourhood.
     * @param name       The name of the neighbourhood.
     * @param background The background image of the neighbourhood.
     */
    public Neighbourhood(int id, String name, String background) {
        this.id = id;
        this.name = name;
        this.background = background;
        this.path = new HashMap<>();
        this.items = new ArrayList<>();
    }

    @Override
    public boolean equals(Object obj) {
        Neighbourhood other = (Neighbourhood) obj;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    /**
     * Retrieves the id of the neighbourhood.
     *
     * @return The id of the neighbourhood.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Retrieves the name of the neighbourhood.
     *
     * @return The name of the neighbourhood.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Retrieves the filename of the background image of the neighbourhood.
     *
     * @return The filename of the background image of the neighbourhood.
     */
    public String getBackground() {
        return this.background;
    }

    /**
     * Retrieves the costumes with the specified name.
     *
     * @param name The name of the costume.
     * 
     * @return The costume with the specified name.
     */
    public Costume getCostumeByName(String name) {
        for (Item x: items) {
            if (x instanceof Costume && x.getName().equals(name)) {
                return (Costume)x;
            }
        }
        return null;
    }

    /**
     * Retrieves the items situated in the neighbourhood.
     *
     * @return The items situated in the neighbourhood.
     */
    public ArrayList<Item> getItems() {
        return this.items;
    }

    /**
     * Retrieves the npc situated in the neighbourhood.
     *
     * @return The npc situated in the neighbourhood.
     */
    public NPC getNPC() {
        return this.npc;
    }

    /**
     * Set the NPC situation in the neighbourhood.
     *
     * @param newNpc the npc to set
     */
    public void setNPC(NPC newNpc) {
        npc = newNpc;
    }


    /**
     * Adds a path in a specified direction from the current neighbourhood for player movement.
     *
     * @param dir  The direction of the path.
     * @param dest The destination (neighbourhood referenced by id) of the path.
     */
    public void addPath(String dir, int dest) {
        Map<String, KeyCode> convertToKeyCode = Map.of(
                "up", KeyCode.UP,
                "down", KeyCode.DOWN,
                "left", KeyCode.LEFT,
                "right", KeyCode.RIGHT
        );
        KeyCode kc = convertToKeyCode.get(dir);
        path.put(kc, dest);
    }

    /**
     * Retrieves the destination (neighbourhood referenced by id) for player movement based on the direction of movement.
     *
     * @param kc The KeyCode representing the direction of movement.
     * @return The destination (neighbourhood referenced by id).
     */
    public int takePath(KeyCode kc) {
        if (path.containsKey(kc)) {
            return path.get(kc);
        } else {
            return -1;
        }
    }

    /**
     * Adds a costume to the neighbourhood.
     *
     * @param x The Costume to be added.
     */
    public void addItem(Item x) {
        items.add(x);
    }

    /**
     * Removes an item from the neighbourhood.
     *
     * @param item the item to be removed.
     */
    public void removeItem(Item item) {
        items.remove(item);
    }

}
