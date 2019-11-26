import java.net.URL;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONModel implements Model {

    private WorldController controller;
    private Player player;
    private HashMap<String, Sector> map = new HashMap<String, Sector>();
    private HashMap<String, Item> items = new HashMap<String, Item>();

    public JSONModel(WorldController controller, Player player) {
        this.controller = controller;
        this.player = player;
    }

    // this method simply goes through all the JSON file fields and saves them in the right place
    public void parseFile(String mapFilePath) {
        try {

            // convert sectors.json to url object
            URL url = this.getClass().getResource(mapFilePath);
            // fetch JSON object from given URL using JSONReader
            JSONObject jsonContent = JSONReader.getJSON(url);

            // Cast content pieces to separate json objects
            JSONObject jPlayer = null;
            JSONObject jItems = null;
            JSONObject jSectors = null;

            if (jsonContent != null) {
                jPlayer = (JSONObject) jsonContent.get("Player");
                jItems = (JSONObject) jsonContent.get("Items");
                jSectors = (JSONObject) jsonContent.get("Sectors");
            }

            // general note: the reason we have to iterate over each sector twice is because the surfaces use sector ids for their entrances so the sector ids must be generated first

            if (jItems != null) {

                // iterate over all items and give each a unique id, and item object
                for (Iterator i = jItems.keys(); i.hasNext(); ) {
                    String id = (String) i.next();
                    if (!items.containsKey(id)) {
                        JSONObject jItem = (JSONObject) jItems.get(id);
                        String displayName = (String) jItem.get("Name");
                        if (displayName != null) {
                            Item item = new Item(new DrawableItem(id, displayName));
                            items.put(id, item);
                        } else {
                            throw new java.lang.RuntimeException("Failed to find display name or text for item " + id);
                        }
                    } else {
                        throw new java.lang.RuntimeException("Failed to create custom item object for " + id);
                    }
                }
            }
            if (jSectors != null) {
                // iterate over all sectors and give each a unique id, and a name corresponding to the JSON name
                for (Iterator l = jSectors.keys(); l.hasNext(); ) {
                    String id = (String) l.next();
                    if (!map.containsKey(id)) {
                        JSONObject jSector = (JSONObject) jSectors.get(id);
                        String name = (String) jSector.get("Name");
                        String text = (String) jSector.get("Text");
                        if (name != null && text != null) {
                            Sector sector = new Sector(id, name, text);
                            map.put(id, sector);
                        } else {
                            throw new java.lang.RuntimeException("Failed to find display name or text for sector " + id);
                        }
                    } else {
                        throw new java.lang.RuntimeException("Failed to create custom sector object for " + id);
                    }
                }

                // iterate over all the sectors again and populate their surfaces / contents
                for (Map.Entry mapElement : map.entrySet()) {

                    String id = (String) mapElement.getKey();
                    Sector sector = (Sector) mapElement.getValue();
                    JSONObject jProperties = (JSONObject) jSectors.get(id);
                    JSONObject jSurfaces = (JSONObject) jProperties.get("Surfaces");

                    for (Iterator j = jSurfaces.keys(); j.hasNext(); ) {
                        String strDir = (String) j.next();
                        JSONObject jDirection = (JSONObject) jSurfaces.get(strDir);
                        if (jDirection != null) {
                            Direction direction = Direction.valueOf(strDir.toUpperCase());
                            Surface surface = new Surface(direction, (String) jProperties.get("Images"));
                            // check if the direction has entrances
                            if (jDirection.has("Entrances")) {
                                JSONArray entrances = (JSONArray) jDirection.get("Entrances");
                                // check that entrances array is not empty
                                if (entrances != null) {
                                    if (entrances.length() != 0) {
                                        // loop through entrances and add their corresponding sector to the surface's entrance list
                                        surface.setEntrances(new ArrayList<Sector>());
                                        for (int k = 0; k < entrances.length(); k++) {
                                            String key = entrances.getString(k);
                                            if (key != null) {
                                                if (map.get(key) != null) {
                                                    surface.getEntrances().add(map.get(key));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (jDirection.has("Contents")) {
                                JSONArray contents = (JSONArray) jDirection.get("Contents");
                                addItemsToContainer(contents, surface);
                            }
                            sector.setSurface(direction, surface);
                        }
                    }

                    map.replace(id, sector);
                }
            }
            if (jPlayer != null) {
                player.setDirection(Direction.valueOf(((String) jPlayer.get("Direction")).toUpperCase()));
                player.setSector(map.get((String) jPlayer.get("Spawn")));
                JSONArray jContents = (JSONArray) jPlayer.get("Contents");
                addItemsToContainer(jContents, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addItemsToContainer(JSONArray contents, Surface surface) {
        if (contents != null) {
            if (contents.length() != 0) {
                for (int k = 0; k < contents.length(); k++) {
                    String key = contents.getString(k);
                    if (key != null) {
                        Item item = items.get(key);
                        if (item != null) {
                            if (surface != null) {
                                surface.addToInventory(item);
                            } else {
                                player.addToInventory(item);
                            }
                        }
                    }
                }
            }
        }
    }

    public void surfaceUpdate(String id) {
        // same sector direction change
        Sector sector = null;
        // since id is either a direction string or id, this determines which one
        Direction side = player.getSideDirection(id);
        // turn
        if (side != null) {
            if (player.getSector() != null) {
                if (player.getSector().getSurface(side) != null) {
                    player.setDirection(side);
                    sector = player.getSector();
                } else {
                    sector = null;
                }
            }
            // sector change
        } else {
            sector = map.get(id);
        }

        // the two necessary conditions for a surfaceChange, either the sector has changed, or the direction has changed AND the direction must have a surface
        if (sector != null && (sector.getSurface(side) != null || side == null)) {
            player.setSector(sector);
            ArrayList<Sector> entrances = null;
            if (sector.getSurface(player.getDirection()) != null) {
                entrances = sector.getSurface(player.getDirection()).getEntrances();
            }
            TreeMap<String, String> choices = new TreeMap<String, String>();
            if (entrances != null) {
                for (int i = 0; i < entrances.size(); i++) {
                    choices.put(entrances.get(i).getId(), entrances.get(i).getDisplayText());
                }
            }
            ArrayList<String> availableSides = new ArrayList<String>();
            // check which surfaces relative to the player are available (for arrow updating)
            for (Direction dir : Direction.values()) {
                if (sector.getSurface(dir) != null) {
                    if (dir == player.getSideDirection("left")) {
                        availableSides.add("left");
                    } else if (dir == player.getSideDirection("right")) {
                        availableSides.add("right");
                    } else if (dir == player.getSideDirection("back")) {
                        availableSides.add("back");
                    }
                }
            }
            controller.surfaceChange(choices, availableSides);
        }
    }

    public void updateContainers(String id) {
        // player has put down an item
        if (player.getContents().containsKey(id)) {
            player.removeFromInventory(items.get(id));
            player.getSector().getSurface(player.getDirection()).addToInventory(items.get(id));
            // player has picked up an item (id = "" is used for initial population of inventory)
        } else if (id != "") {
            player.getSector().getSurface(player.getDirection()).removeFromInventory(items.get(id));
            player.addToInventory(items.get(id));
        }
        controller.inventoryChange();
    }

    // return items in inventory
    public HashMap<String, Item> getContents(String surface) {
        // player contents
        if (surface != "") {
            return player.getContents();
            // surface contents
        } else if (surface != null) {
            return player.getSector().getSurface(player.getDirection()).getContents();
        }
        return null;
    }

    public boolean surfaceAvailable() { return (getSurface() != null); }
    public Integer getInventoryLimit() {
        return player.getInventoryLimit();
    }
    public String getDirection(String string) { return player.getDirection().toString().toLowerCase(); }
    public Direction getDirection() { return player.getDirection(); }
    public String getAreaID() {
        return player.getSector().getId();
    }
    public String getAreaName() {
        return player.getSector().getDisplayName();
    }
    public String getPlayerName() {
        return player.getName();
    }
    public String getAreaText() {
        return player.getSector().getDisplayText();
    }
    public Surface getSurface(Direction dir) {
        return player.getSector().getSurface(dir);
    }
    public Surface getSurface() { return player.getSector().getSurface(player.getDirection()); }
}
