import java.util.ArrayList;
import java.util.HashMap;

public class Surface implements Inventory {

    private String imagePath;
    private Direction direction;
    private ArrayList<Sector> entrances;
    private HashMap<String, Item> contents = new HashMap<String, Item>();

    public Surface(Direction direction, String image) {
        this.direction = direction;
        // replace "direction" in "name-direction.format" with for e.g. "north"
        this.imagePath = image.replace("direction", direction.toString().toLowerCase());
    }

    public String getImagePath() { return imagePath; }
    public Direction getDirection() { return direction; }
    public ArrayList<Sector> getEntrances() {
        return entrances;
    }

    public void setEntrances(ArrayList<Sector> entrances) {
        this.entrances = entrances;
    }

    @Override
    public void addToInventory(Item item) {
        contents.put(item.getId(), item);
    }

    @Override
    public void removeFromInventory(Item item) { contents.remove(item.getId()); }

    @Override
    public HashMap<String, Item> getContents() {
        return contents;
    }
}
