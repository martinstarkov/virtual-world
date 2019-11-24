import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FXMLController implements ViewController {

    WorldController worldController;

    @FXML
    private ImageView imageView;
    @FXML
    private TextArea console;
    @FXML
    private Text directionIndicator;
    @FXML
    private Text lastAction;
    @FXML
    private HBox arrowBox;
    @FXML
    private HBox toolBoxHBox;
    @FXML
    private Menu putDownMenu;
    @FXML
    private Menu pickUpMenu;

    private String previousAreaID = "";

    @Override
    public void setWorldController(WorldController worldController) {
        this.worldController = worldController;
    }

    @Override
    public void updateDisplay(ImageObject newImage) {
        imageView.setImage(newImage.getJavaFxImage());
    }

    @Override
    public void updateInterface(HashMap<String, String> choices, ArrayList<String> availableSides) {
        // cycle through arrows and change the color to green if they are an available side
        for (Node node : arrowBox.getChildren()) {
            Button button = (Button) node;
            if (availableSides.contains(button.getId())) {
                // available button
                button.setStyle("-fx-background-color: green; -fx-shape: \"M 0 -3.5 v 7 l 4 -3.5 z\";");
                button.setDisable(false);
            } else {
                // disabled button
                button.setStyle("-fx-background-color: grey; -fx-shape: \"M 0 -3.5 v 7 l 4 -3.5 z\";");
                button.setDisable(true);
            }
        }
        // remove all buttons between updates
        toolBoxHBox.getChildren().clear();
        for (Map.Entry choice : choices.entrySet()) {
            String id = (String) choice.getKey();
            String displayText = (String) choice.getValue();
            Button button = new Button(displayText);
            button.setId(id);
            toolBoxHBox.setHgrow(button, Priority.ALWAYS);
            button.setMaxWidth(Double.MAX_VALUE);
            EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    buttonPressed(event);
                }
            };
            button.setOnAction(event);
            toolBoxHBox.getChildren().add(button);
        }
    }

    // this method handles updating all text-based UI elements
    @Override
    public void updateText(String playerName, String areaID, String areaName, String actionText, Direction direction) {
        // area is the same as the last one (only direction has changed)
        if (areaID == previousAreaID) {
            console.appendText(playerName + " has turned " + direction.toString().toLowerCase() + "." + "\n");
            lastAction.setText("Last Action: turned " + direction.toString().toLowerCase());
            // new area
        } else {
            console.appendText(playerName + " has entered " + areaName + "." + "\n");
            lastAction.setText("Last Action: pressed button \"" + actionText + "\"");
        }
        directionIndicator.setText("Direction: " + direction.toString());
        previousAreaID = areaID;
    }

    @Override
    public void updateMenu(HashMap<String, Item> playerInventory, HashMap<String, Item> areaInventory) {
        putDownMenu.getItems().clear();
        for (Map.Entry content : playerInventory.entrySet()) {
            String id = (String) content.getKey();
            Item item = (Item) content.getValue();
            MenuItem menuItem = new MenuItem(item.getDisplayName());
            menuItem.setId(id);
            EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    itemAction(event);
                }
            };
            menuItem.setOnAction(event);
            putDownMenu.getItems().add(menuItem);
        }
        pickUpMenu.getItems().clear();
        for (Map.Entry content : areaInventory.entrySet()) {
            String id = (String) content.getKey();
            Item item = (Item) content.getValue();
            MenuItem menuItem = new MenuItem(item.getDisplayName());
            menuItem.setId(id);
            EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    itemAction(event);
                }
            };
            menuItem.setOnAction(event);
            pickUpMenu.getItems().add(menuItem);
        }
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
    @FXML
    public void itemAction(ActionEvent e) {
        MenuItem item = (MenuItem) e.getSource();
        String id = item.getId();
        worldController.stateChange(id);
    }
}
