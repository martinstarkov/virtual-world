
import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;


public class Model {
    private WorldController controller;
    private Player player;
    private HashMap<String, Sector> map = new HashMap<String, Sector>();
    public Model(WorldController controller, Player player) {
        this.controller = controller;
        this.player = player;
    }
    public void parseFile(String mapFilePath) {
        try {
            URL path = this.getClass().getResource("sectors.json");
            File file = Paths.get(path.toURI()).toFile();
            // Convert JSON file contents to string
            String content = FileUtils.readFileToString(file, "utf-8");
            JSONObject jsonContent = new JSONObject(content);
            // Cast content pieces to separate json objects
            JSONObject jPlayer = (JSONObject) jsonContent.get("Player");
            JSONObject jItems = (JSONObject) jsonContent.get("Items");
            JSONObject jSectors = (JSONObject) jsonContent.get("Sectors");
            // general note: the reason we have to iterate over each sector twice is because the surfaces use sector ids for their entrances so the sector ids must be generated first

            // iterate over all sectors and give each a unique id, and a name corresponding to the JSON name
            for (Iterator i = jSectors.keys(); i.hasNext();) {
                String id = (String) i.next();
                if (!map.containsKey(id)) {
                    JSONObject jSector = (JSONObject) jSectors.get(id);
                    String name = (String) jSector.get("Name");
                    String text = (String) jSector.get("Text");
                    if (name != null && text != null) {
                        Sector sector = new Sector(id, name, text);
                        map.put(id, sector);
                    } else {
                        throw new java.lang.RuntimeException("Failed to find display name or text for " + id);
                    }
                } else {
                    throw new java.lang.RuntimeException("Failed to create custom sector object for " + id);
                }
            }

            // iterate over all the sectors again and populate their surfaces
            for (Map.Entry mapElement : map.entrySet()) {

                String id = (String) mapElement.getKey();
                Sector sector = (Sector) mapElement.getValue();
                JSONObject jProperties = (JSONObject) jSectors.get(id);
                JSONObject jSurfaces = (JSONObject) jProperties.get("Surfaces");

                for (Iterator j = jSurfaces.keys(); j.hasNext();) {
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
                        sector.setSurface(direction, surface);
                    }
                }

                /*
                Old implementation of adding left, back, and right choices to choices array.

                for (int d = 0; d < Direction.values().length; d++) {
                    Direction dir = Direction.values()[d];
                    if (sector.getSurface(dir) != null) {
                        if (sector.getSurface(Direction.values()[(d + 1) % Direction.values().length]) != null) {
                            sector.getSurface(dir).addEntrance(-999, new Sector("right", dir.toString(), "Turn Right"));
                        }
                        if (sector.getSurface(Direction.values()[(d + 2) % Direction.values().length]) != null) {
                            sector.getSurface(dir).addEntrance(sector.getSurface(dir).getEntrances().size() / 2, new Sector("back", dir.toString(), "Turn Around"));
                        }
                        if (sector.getSurface(Direction.values()[(d + 3) % Direction.values().length]) != null) {
                            sector.getSurface(dir).addEntrance(0, new Sector("left", dir.toString(), "Turn Left"));
                        }
                    }
                }
                */

                map.replace(id, sector);
            }

            player.setDirection(Direction.valueOf(((String) jPlayer.get("Direction")).toUpperCase()));
            player.setSector(map.get((String) jPlayer.get("Spawn")));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printMap(String part) {
        for (Map.Entry mapElement : map.entrySet()) {
            Sector sector = (Sector) mapElement.getValue();
            if (sector != null) {
                for (Direction dir : Direction.values()) {
                    if (sector.getSurface(dir) != null) {
                        if (part == "sectors") {
                            System.out.println(sector.getId() + ": (" + sector.getDisplayName() + ")");
                            break;
                        } else if (part == "images") {
                            if (sector.getSurface(dir).getImagePath() != null) {
                                System.out.println(sector.getId() + " [" + dir.toString() + "]: " + sector.getSurface(dir).getImagePath());
                            }
                        } else if (part == "entrances") {
                            if (sector.getSurface(dir).getEntrances() != null) {
                                System.out.println(sector.getId() + " [" + dir.toString() + "]: " + Arrays.asList(sector.getSurface(dir).getEntrances()).get(0).get(0).getDisplayText());
                            }
                        } else if (part == "contents") {
                            // add later
                        }
                    }
                }
            }
        }
    }

    public void surfaceChanged(String id) {

        // change this to match player direction

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
            //System.out.println("Sector changed to: " + sector.getDisplayName());
        }

        //System.out.println("Sector: " + sector.getDisplayName() + ", Direction: " + player.getDirection());

        // the two necessary conditions for a surfaceChange, either the sector has changed, or the direction has changed AND the direction must have a surface
        if (sector != null && (sector.getSurface(side) != null || side == null)) {
            player.setSector(sector);
            controller.surfaceChanged(player.getSector(), player.getDirection(), sector.getSurface(player.getDirection()).getEntrances());
        }
    }

    private boolean containsSector(ArrayList<Sector> list, String side) {
        for (int i = 0; i < list.size(); i++) {
            String name = list.get(i).getDisplayName();
            if (name == side) {
                return true;
            }
        }
        return false;
    }
}
