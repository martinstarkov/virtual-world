import java.util.ArrayList;

public class Surface implements Inventory {
    private String imagePath;
    private Direction direction;
    private ArrayList<Sector> entrances;
    //private contents
    //private entrances
    public Surface(Direction direction, String image) {
        this.direction = direction;
        // replace "direction" in "name-direction.format" with for e.g. "north"
        this.imagePath = image.replace("direction", direction.toString().toLowerCase());
    }
    public String getImagePath() {
        return imagePath;
    }
    public Direction getDirection() {
        return direction;
    }
    public void setEntrances(ArrayList entrances) {
        this.entrances = entrances;
    }
    public ArrayList<Sector> getEntrances() {
        return entrances;
    }
}
