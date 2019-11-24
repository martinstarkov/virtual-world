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
		stateChange("");
	}

	public void surfaceSelected(String id) {
		model.surfaceChanged(id);
	}
	// called by the model; tells the view to update UI with new button choices corresponding to new area
	public void surfaceChanged(HashMap<String, String> choices, ArrayList<String> availableSides) {
		if (player.getSector().getSurface(player.getDirection()) != null) {
			viewController.updateMenu(player.getContents(), player.getSector().getSurface(player.getDirection()).getContents());
		} else {
			viewController.updateMenu(player.getContents(), null);
		}
		viewController.updateInterface(choices, availableSides);
		viewController.updateText(player.getName(), player.getSector().getId(), player.getSector().getDisplayName(), player.getSector().getDisplayText(), player.getDirection());
		ImageObject newImage = service.getImage(player.getSector().getId(), player.getDirection());
		viewController.updateDisplay(newImage);
	}

    public void stateChange(String id) {
		model.updateContainers(id);
    }

	public void inventoryUpdate() {
		//viewController.updateInventory();
		viewController.updateMenu(player.getContents(), player.getSector().getSurface(player.getDirection()).getContents());
	}
}
