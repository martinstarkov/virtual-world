import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VirtualWorld extends Application {

    private static ViewController viewController;
    private static WorldController worldController;

    public static void main(String args[]) {
        worldController = new WorldController();
        worldController.initialise();
        // if not using javafx, make sure you call
        // worldController.setup(viewController);
        // here with your custom ViewController implementation
        launch(args);
        System.exit(0);
    }

    @Override
    public void start(Stage stage) {
        try {
            // change fxml file location here
            URL url = this.getClass().getResource("CustomWorldViewer.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            BorderPane page = (BorderPane) fxmlLoader.load();
            Scene scene = new Scene(page);
            viewController = (FXMLController) fxmlLoader.getController();
            // initialise view controller and initial interface conditions
            worldController.setup(viewController);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }
}
