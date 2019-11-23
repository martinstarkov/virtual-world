import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class WorldController {

	private Player player;
	private Model model;
	private LocalService service;
	private ViewController viewController;

	public void initialise() {

		player = new Player("Martin");
		model = new Model(this, player);
		model.parseFile("sectors.json");
		service = new LocalService("resources");
		// model.printMap("entrances");
	    // image = service.getImage(player.getSector().getName(), 0);
	}

	public void setupViewController(ViewController vc) {
		this.viewController = vc;
		viewController.setWorldController(this);
		surfaceSelected(player.getSector().getId());
	}

	public void surfaceSelected(String id) {
		model.surfaceChanged(id);
	}
	// called by the model; tells the view to update UI with new button choices corresponding to new sector
	public void surfaceChanged(Sector sector, Direction direction, HashMap<String, String> choices) {
		viewController.updateInterface(choices);
		viewController.updateText(player.getName(), sector, direction);
		ImageObject newImage = service.getImage(player.getSector().getId(), player.getDirection());
		viewController.updateDisplay(newImage);
	}

}
