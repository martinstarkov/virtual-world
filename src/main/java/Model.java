import java.util.HashMap;

public interface Model {

    void parseFile(String mapFilePath);
    void surfaceUpdate(String id);
    void updateContainers(String id);
    boolean surfaceAvailable();
    String getDirection(String formatted);
    Direction getDirection();
    HashMap<String, Item> getContents(String surface);
    Integer getInventoryLimit();
    String getAreaID();
    String getAreaName();
    String getAreaText();
    String getPlayerName();
}
