import java.util.HashMap;

public class Sector {

    private String id;
    private String displayName;
    private String displayText;
    // hash map with surface objects
    private HashMap<Direction, Surface> surfaces = new HashMap<Direction, Surface>();

    public Sector(String id, String displayName, String displayText) {
        this.id = id;
        this.displayName = displayName;
        this.displayText = displayText;
    }

    public String getDisplayName() {
        return displayName;
    }
    public String getDisplayText() {
        return displayText;
    }
    public String getId() {
        return id;
    }
    public Surface getSurface(Direction direction) {
        return surfaces.get(direction);
    }

    public void setSurface(Direction direction, Surface surface) {
        surfaces.put(direction, surface);
    }
}
