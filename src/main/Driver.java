package main;

import java.io.*;
import java.util.*;

import main.items.Costume;
import main.items.Item;
import org.json.*;

import builder.*;
import main.character.Player;

public class Driver {

    private static Player player;
    private static HashMap<Integer, Neighbourhood> map;

    final static String DATA_PATH = "./src/resources/data/data.json";

    public static HashMap<Integer, Neighbourhood> loadMap() throws JSONException, IOException {
        NeighbourhoodBuilder nb = new NeighbourhoodBuilder(DATA_PATH);
        return nb.exportMap();
    }

    public static void init() throws Exception {
        player = new Player();
        map = loadMap();
        for (Item item : map.get(0).getItems()) {
            if (item instanceof Costume) {
                player.addCostume((Costume)item);
            }
        }
    }

    public static Player getPlayer() { return player; }

    public static HashMap<Integer, Neighbourhood> getMap() { return map; }
}
