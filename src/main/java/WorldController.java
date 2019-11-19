import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class WorldController {

	private Player player;
	private Model model;
	private LocalService service;
	private ViewController viewController;

	public void initialise() {

		player = new Player("Martin");
		model = new Model(this, player);
		model.parseFile("src/main/resources/sectors.json");
		service = new LocalService("src/main/resources/sector_images/");
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
        ImageObject newImage = service.getImage(player.getSector().getId(), player.getDirection());
        viewController.updateDisplay(newImage);
	}
	// called by the model; tells the view to update UI with new button choices corresponding to new sector
	public void surfaceChanged(ArrayList choices) {
		viewController.updateInterface(choices);
	}

}
