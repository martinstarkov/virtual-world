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
import java.util.TreeMap;

// This class is essentially the view, I did not have enough time to make an abstract ViewController buffer for the WorldController.

public class FXMLController implements ViewController {

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
    @FXML
    private GridPane inventory;
    @FXML
    private FlowPane roomInventory;

    private WorldController worldController;
    private String previousAreaID = "";

    @Override
    public void setWorldController(WorldController worldController) {
        this.worldController = worldController;
    }

    @Override
    public void updateDisplay(ImageObject newImage) {
        if (newImage != null) {
            imageView.setImage(newImage.getImage());
        }
    }

    // update arrows and entrance choice button
    @Override
    public void updateInputInterface(TreeMap<String, String> choices, ArrayList<String> availableSides) {
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
        // remove all entrance choice buttons between updates
        toolBoxHBox.getChildren().clear();

        // update entrance choice buttons
        for (Map.Entry choice : choices.entrySet()) {
            String id = (String) choice.getKey();
            String displayText = (String) choice.getValue();
            Button button = new Button(displayText);
            button.setId(id);
            // make buttons fill as much of the tool bar as they can (if two or more, they share the space)
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

    // this method handles updating console (text box) and menu bar indicators
    @Override
    public void updateText(String playerName, String areaID, String areaName, String actionText, String direction) {
        // area is the same as the last one (only direction has changed)
        if (areaID == previousAreaID) {
            console.appendText(playerName + " has turned " + direction + "." + "\n");
            lastAction.setText("Last Action: turned " + direction);
            // new area
        } else {
            // so many of the walkways are called "Path" that I decided to log only non-path names in console as their name
            if (areaName != "Path") {
                console.appendText(playerName + " has entered area with id: " + areaID + "\n");
            } else {
                console.appendText(playerName + " has entered: " + areaName + "\n");
            }
            lastAction.setText("Last Action: pressed button \"" + actionText + "\"");
        }
        // top bar indicator of direction
        directionIndicator.setText("Direction: " + direction.toUpperCase());
        // used for checking if sector changed or direction changed
        previousAreaID = areaID;
    }

    @Override
    public void updateMenu(Integer playerInventorySpace, HashMap<String, Item> playerInventory, HashMap<String, Item> areaInventory) {
        // clear put down menu
        putDownMenu.getItems().clear();
        // clear pick up menu
        pickUpMenu.getItems().clear();
        // clear room inventory
        roomInventory.getChildren().clear();
        // clear player inventory
        inventory.getChildren().clear();
        // perfect square grid
        double squareSize = Math.sqrt(playerInventorySpace);
        int rows;
        int columns;
        int inventorySpace;
        // check that inventory size is perfect square and not 0
        if ((squareSize - Math.floor(squareSize)) == 0 && squareSize != 0) {
            rows = (int) squareSize;
            columns = (int) squareSize;
            inventorySpace = rows * columns;
        } else {
            // Set 9 if provided player inventory space is not perfect square
            rows = 3;
            columns = 3;
            inventorySpace = rows * columns;
        }
        // set starting row and column numbers
        int currentRow = 0;
        int currentColumn = 0;
        // check if new item can be fit into inventory and that there are items to display
        if (playerInventory.size() >= inventorySpace || areaInventory.size() == 0) {
            pickUpMenu.setDisable(true);
            if (playerInventory.size() >= inventorySpace) {
                console.appendText("Inventory full, cannot pick up anymore items!" + "\n");
            }
        } else {
            pickUpMenu.setDisable(false);
        }
        // player inventory update
        for (Map.Entry content : playerInventory.entrySet()) {
            // go through items and add the display names to the put down up menu
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
            if (inventorySpace > 0) {
                // add item object to player inventory box
                ImageObject itemImage = worldController.imageChange(item.getId(), "");
                ImageView view = new ImageView();
                view.fitWidthProperty().bind(inventory.widthProperty().divide(columns));
                view.fitHeightProperty().bind(inventory.heightProperty().divide(rows));
                view.setImage(itemImage.getImage());
                inventory.add(view, currentColumn, currentRow);
                // move to the next row after the last column is reached (grid)
                if (currentColumn == columns - 1) {
                    currentColumn = 0;
                    currentRow++;
                } else {
                    currentColumn++;
                }
                inventorySpace--;
            }
        }
        // room inventory update
        if (areaInventory.size() != 0) {
            for (Map.Entry content : areaInventory.entrySet()) {
                // go through items and add the display names to the pick up menu
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
                // add item image to room inventory box
                ImageObject itemImage = worldController.imageChange(item.getId(), "");
                ImageView view = new ImageView();
                view.fitWidthProperty().bind(inventory.widthProperty().divide(columns));
                view.fitHeightProperty().bind(inventory.heightProperty().divide(rows));
                view.setImage(itemImage.getImage());
                roomInventory.getChildren().add(view);
            }
        }
    }

    // when a javafx button is pressed, it will call this method
    @FXML
    public void buttonPressed(ActionEvent e) {
        Button button = (Button) e.getSource();
        String id = button.getId();
        // disable button temporarily to prevent spam-click overloading
        button.setDisable(true);
        // send id of pressed button to controller
        worldController.surfaceSelected(id);
        // enable button once surface selection has been fully processed
        button.setDisable(false);
    }
    // when a javafx menu bar (pick up, put down) menu item is pressed, this method will be called
    @FXML
    public void itemAction(ActionEvent e) {
        MenuItem item = (MenuItem) e.getSource();
        String id = item.getId();
        // tell controller that game state (inventories) have changed
        worldController.stateChange(id);
    }
}
