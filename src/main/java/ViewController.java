import java.util.ArrayList;

public interface ViewController {
    void setWorldController(WorldController worldController);
    void updateDisplay(ImageObject newImage);
    void updateInterface(ArrayList choices);
}