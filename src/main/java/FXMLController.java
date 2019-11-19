import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.Arrays;

public class FXMLController implements ViewController {

    WorldController worldController;

    @FXML
    private AnchorPane ap;
    @FXML
    private ImageView imageView;

    public void setWorldController(WorldController worldController) {
        this.worldController = worldController;
    }

    public void updateDisplay(ImageObject newImage) {
        imageView.setImage(newImage.getJavaFxImage());
    }
    public void updateInterface(ArrayList newChoices) {
        ObservableList<Node> nodes = ap.getChildren();
        ArrayList<Node> removeNodes = new ArrayList<Node>();
        for (Object child : nodes) {
            // check if Node is an instance of CheckBox.
            if (child != null && child instanceof Button) {
                removeNodes.add((Node) child);
            }
        }
        for (Node child : removeNodes) {
            ap.getChildren().remove(child);
        }
        if (newChoices != null) {
            Button lastButton = new Button();
            for (int i = 0; i < newChoices.size(); i++) {
                Sector sector = (Sector) newChoices.get(i);
                Button button;
                if (newChoices.size() == 1) {
                    button = new Button("Go Back");
                } else {
                    button = new Button(sector.getDisplayText());
                }
                button.setId(sector.getId());
                ap.getChildren().add(button);
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

    // when a javafx button is pressed, it will call this method
    public void buttonPressed(ActionEvent e) {
        Button button = (Button) e.getSource();
        String id = button.getId();
        // send id of pressed button to controller
        worldController.surfaceSelected(id);
    }
}
