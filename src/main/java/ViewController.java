import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public interface ViewController {

    void setWorldController(WorldController worldController);
    void updateDisplay(ImageObject newImage);
    void updateInputInterface(TreeMap<String, String> choices, ArrayList<String> availableSides);
    void updateText(String playerName, String areaID, String areaName, String actionText, String direction);
    void updateMenu(Integer playerInventorySpace, HashMap<String,Item> playerInventory, HashMap<String,Item> areaInventory);
}