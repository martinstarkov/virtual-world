import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FXMLController implements ViewController {

    WorldController worldController;

    @FXML
    private BorderPane ap;
    @FXML
    private ImageView imageView;
    @FXML
    private TextArea console;
    @FXML
    private ToolBar choiceBox;
    @FXML
    private Text directionIndicator;
    @FXML
    private Text lastAction;

    private Sector previousSector = new Sector("", "", "");

    @Override
    public void setWorldController(WorldController worldController) {
        this.worldController = worldController;
    }

    @Override
    public void updateDisplay(ImageObject newImage) {
        imageView.setImage(newImage.getJavaFxImage());
    }

    @Override
    public void updateInterface(HashMap<String, String> choices) {
        // remove all buttons between updates
        choiceBox.getItems().clear();
        // counter for displacing buttons' relative positions
        int i = 0;
        for (Map.Entry choice : choices.entrySet()) {
            String id = (String) choice.getKey();
            String displayText = (String) choice.getValue();
            Button button = new Button(displayText);
            button.setId(id);
            EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    buttonPressed(event);
                }
            };
            button.setOnAction(event);
            choiceBox.getItems().add(button);
            i++;
        }
    }

    // this method handles updating all text-based UI elements
    @Override
    public void updateText(String playerName, Sector sector, Direction direction) {
        // sector is the same as the last one (no change)
        if (sector.getId() == previousSector.getId()) {
            console.appendText(playerName + " has turned " + direction.toString().toLowerCase() + "." + "\n");
            lastAction.setText("Last Action: " + direction.toString().toLowerCase() + " turn");
            // new sector
        } else {
            console.appendText(playerName + " has entered " + sector.getDisplayName() + "." + "\n");
            lastAction.setText("Last Action: " + sector.getDisplayText());
        }
        directionIndicator.setText("Direction: " + direction.toString());
        previousSector = sector;
    }

    // when a javafx button is pressed, it will call this method
    @FXML
    public void buttonPressed(ActionEvent e) {
        Button button = (Button) e.getSource();
        String id = button.getId();
        // send id of pressed button to controller
        button.setDisable(true);
        worldController.surfaceSelected(id);
        button.setDisable(false);
    }
}
