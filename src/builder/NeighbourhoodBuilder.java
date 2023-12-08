package builder;

import java.io.*;
import java.util.*;

import java.nio.file.*;

import main.items.Costume;
import main.items.Item;
import org.json.*;

import main.*;
import main.attacks.*;
import main.character.*;

/**
 * NeighbourhoodBuilder class is responsible for constructing Neighbourhood objects from JSON data.
 */
public class NeighbourhoodBuilder implements INeighbourhoodBuilder {

    private HashMap<Integer, Neighbourhood> map;
    private JSONArray neighbourhoods;
    private JSONArray costumes;
    private JSONArray npcs;
    private JSONArray attacks;

    /**
     * Constructor for NeighbourhoodBuilder.
     *
     * @param datapath The path to the JSON data file.
     * @throws IOException   If an I/O error occurs.
     * @throws JSONException If there is an issue with JSON parsing.
     */
    public NeighbourhoodBuilder(String datapath) throws IOException, JSONException {
        JSONObject data = new JSONObject(new String(Files.readAllBytes(Paths.get(datapath))));
        neighbourhoods = data.getJSONArray("neighbourhoods");
        costumes = data.getJSONArray("costume");
        npcs = data.getJSONArray("npc");
        attacks = data.getJSONArray("attack");
        this.build();
    }

    /**
     * Retrieves the constructed map of Neighbourhood objects.
     *
     * @return The map of Neighbourhood objects.
     */
    public HashMap<Integer, Neighbourhood> exportMap() {
        return this.map;
    }

    /**
     * Builds Neighbourhood objects from JSON data.
     *
     * @throws JSONException If there is an issue with JSON parsing.
     */
    public void build() throws JSONException {
        map = new HashMap<>();
        for (int i = 0; i < neighbourhoods.length(); i++) {
            map.put(i, parseNeighbourhood(neighbourhoods.getJSONObject(i)));
        }
    }

    /**
     * Parses JSON data to create a Neighbourhood object.
     *
     * @param neighbourhood The JSON object representing a neighbourhood.
     * @return The constructed Neighbourhood object.
     * @throws NumberFormatException If there is an error in number format.
     * @throws JSONException         If there is an issue with JSON parsing.
     */
    @Override
    public Neighbourhood parseNeighbourhood(JSONObject neighbourhood) throws NumberFormatException, JSONException {
        
        // Extracting attributes from JSON and creating a Neighbourhood object
        int id = Integer.parseInt(neighbourhood.getString("id"));
        String name = neighbourhood.getString("name");
        String background = neighbourhood.getString("background");

        Neighbourhood newNeighbourhood = new Neighbourhood(id, name, background);

        // Parsing costume information and adding costumes to the Neighbourhood
        for (int i = 0; i < neighbourhood.getJSONArray("costume").length(); i++) {
            int costumeId = Integer.parseInt(neighbourhood.getJSONArray("costume").getString(i));
            newNeighbourhood.addItem(parseCostume(costumes.getJSONObject(costumeId)));
        }

        // Parsing NPC information and adding NPCs to the Neighbourhood
        for (int i = 0; i < neighbourhood.getJSONArray("npc").length(); i++) {
            int npcId = Integer.parseInt(neighbourhood.getJSONArray("npc").getString(i));
            newNeighbourhood.setNPC(parseNPC(npcs.getJSONObject(npcId)));
        }

        // Parsing path information and adding paths to the Neighbourhood
        for (int i = 0; i < neighbourhood.getJSONArray("path").length(); i++) {
            String dir = neighbourhood.getJSONArray("path").getJSONObject(i).getString("direction");
            int dest = Integer.parseInt(neighbourhood.getJSONArray("path").getJSONObject(i).getString("id"));
            newNeighbourhood.addPath(dir, dest);
        }

        return newNeighbourhood;
    }

    /**
     * Parses JSON data to create a Costume object.
     *
     * @param costume The JSON object representing a costume.
     * @return The constructed Costume object.
     */
    @Override
    public Costume parseCostume(JSONObject costume) throws NumberFormatException, JSONException {
        
        int id = Integer.parseInt(costume.getString("id"));
        String name = costume.getString("name");
        String type = costume.getString("type");
        String description = costume.getString("description");
        String image = costume.getString("image");

        double hp = Double.parseDouble(costume.getJSONArray("combat").getString(0));
        double damage = Double.parseDouble(costume.getJSONArray("combat").getString(1));
        int defense = Integer.parseInt(costume.getJSONArray("combat").getString(2));

        Costume newCostume = new Costume(id, name, type, description, image, hp, damage, defense);

        for (int i = 0; i < costume.getJSONArray("attacks").length(); i++) {
            int attackId = Integer.parseInt(costume.getJSONArray("attacks").getString(i));
            newCostume.addAttack(parseAttack(attacks.getJSONObject(attackId)));
        }

        return newCostume;
    }

    /**
     * Parses JSON data to create an NPC object.
     *
     * @param npc The JSON object representing an NPC.
     * @return The constructed NPC object.
     */
    @Override
    public NPC parseNPC(JSONObject npc) throws NumberFormatException, JSONException {

        int id = Integer.parseInt(npc.getString("id"));
        String name = npc.getString("name");
        String type = npc.getString("type");
        String prompt = npc.getString("prompt");
        String image = npc.getString("image");

        int hp = Integer.parseInt(npc.getJSONArray("combat").getString(0));
        int def = Integer.parseInt(npc.getJSONArray("combat").getString(1));

        NPC newNPC = new NPC(id, name, type, prompt, image, hp, def);

        // TODO add favourite

        // TODO add hate

        for (int i = 0; i < npc.getJSONArray("attacks").length(); i++) {
            int attackId = Integer.parseInt(npc.getJSONArray("attacks").getString(i));
            newNPC.addAttack(parseAttack(attacks.getJSONObject(attackId)));
        }

        return newNPC;
    }

    /**
     * Parses JSON data to create an Attack object.
     *
     * @param attack The JSON object representing an Attack.
     * @return The constructed Attack object.
     */
    @Override
    public Attack parseAttack(JSONObject attack) throws NumberFormatException, JSONException {
        int id = Integer.parseInt(attack.getString("id"));
        String name = attack.getString("name");
        String type = attack.getString("type");
        String description = attack.getString("description");

        int which = Integer.parseInt(attack.getString("which"));
        int atk = Integer.parseInt(attack.getString("atk"));

        return switch (which) {
            case 1 -> new AttackHeal(id, name, type, description, atk);
            case 2 -> new AttackVampire(id, name, type, description, atk);
            default -> new AttackStandard(id, name, type, description, atk);
        };
    }

    /**
     * Parses JSON data to create an Item object.
     *
     * @param item The JSON object representing an Item.
     * @return The constructed Item object.
     */
    @Override
    public Item parseItem(JSONObject item) {
        // TODO Placeholder implementation
        return null;
    }
}
