import java.util.HashMap;

public interface ViewController {
    void setWorldController(WorldController worldController);
    void updateDisplay(ImageObject newImage);
    void updateInterface(HashMap<String, String> choices);
    void updateText(String playerName, Sector sector, Direction direction);
}