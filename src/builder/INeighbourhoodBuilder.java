package builder;

import main.items.Costume;
import main.items.Item;
import org.json.*;

import main.*;
import main.attacks.*;
import main.character.*;

/**
 * INeighbourhoodBuilder interface is responsible for providing an interface for constructing Neighbourhood objects from JSON data.
 */
public interface INeighbourhoodBuilder {

    /**
     * Parses the JSON representation of a neighbourhood and constructs a Neighbourhood object.
     *
     * @param neighbourhood The JSON object representing a neighbourhood.
     * @return A Neighbourhood object parsed from the JSON.
     * @throws JSONException If there are issues parsing the JSON.
     */
    public abstract Neighbourhood parseNeighbourhood(JSONObject neighbourhood) throws JSONException;

    /**
     * Parses the JSON representation of a costume and constructs a Costume object.
     *
     * @param costume The JSON object representing a costume.
     * @return A Costume object parsed from the JSON.
     * @throws JSONException If there are issues parsing the JSON.
     */
    public abstract Costume parseCostume(JSONObject costume) throws JSONException;

    /**
     * Parses the JSON representation of an NPC (Non-Player Character) and constructs an NPC object.
     *
     * @param npc The JSON object representing an NPC.
     * @return An NPC object parsed from the JSON.
     * @throws JSONException If there are issues parsing the JSON.
     */
    public abstract NPC parseNPC(JSONObject npc) throws JSONException;

    /**
     * Parses the JSON representation of an attack and constructs an Attack object.
     *
     * @param attack The JSON object representing an attack.
     * @return An Attack object parsed from the JSON.
     * @throws JSONException If there are issues parsing the JSON.
     */
    public abstract Attack parseAttack(JSONObject attack) throws JSONException;

    /**
     * Parses the JSON representation of an item and constructs an Item object.
     *
     * @param item The JSON object representing an item.
     * @return An Item object parsed from the JSON.
     * @throws JSONException If there are issues parsing the JSON.
     */
    public abstract Item parseItem(JSONObject item) throws JSONException;
}
