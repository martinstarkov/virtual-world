import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.ArrayList;

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
    public void updateInterface(ArrayList newChoices) {
        // remove all buttons between updates
        choiceBox.getItems().clear();
        if (newChoices != null) {
            Button lastButton = new Button();
            for (int i = 0; i < newChoices.size(); i++) {
                Sector sector = (Sector) newChoices.get(i);
                Button button;
                button = new Button(sector.getDisplayText());
                button.setId(sector.getId());
                choiceBox.getItems().add(button);
                EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        buttonPressed(event);
                    }
                };
                button.setOnAction(event);
                button.setLayoutX(lastButton.getLayoutX() + i * 70);
                button.setLayoutY(14.0);
                lastButton = button;
            }
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
