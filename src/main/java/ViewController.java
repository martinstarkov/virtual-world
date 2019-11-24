import java.util.ArrayList;
import java.util.HashMap;

public interface ViewController {
    void setWorldController(WorldController worldController);
    void updateDisplay(ImageObject newImage);
    void updateInterface(HashMap<String, String> choices, ArrayList<String> availableSides);
    void updateText(String playerName, String areaID, String areaName, String actionText, Direction direction);
    void updateMenu(HashMap<String,Item> playerInventory, HashMap<String,Item> areaInventory);
}