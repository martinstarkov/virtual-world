import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class WorldController {

	private Player player;
	private JSONModel model;
	private LocalService service;
	private ViewController viewController;

	// setup MVC components and player
	public void initialise() {

	    // inventoryLimit must be a perfect square, otherwise override to 9 by default
		player = new Player("Martin", 4);
		// set custom model here
		model = new JSONModel(this, player);
		model.parseFile("sectors.json");
		// set custom service here
		service = new LocalService();
	}

	// called in the VirtualWorld class when the application start() method has been called
	public void setup(ViewController vc) {
		this.viewController = vc;
		viewController.setWorldController(this);
		// initial setup of interface and inventories
		surfaceSelected(model.getAreaID());
		stateChange("");
	}

	public void surfaceSelected(String id) {
		model.surfaceUpdate(id);
	}

	public void inputInterfaceChange(TreeMap<String, String> choices, ArrayList<String> availableSides) {
		viewController.updateInputInterface(choices, availableSides);
	}

	public ImageObject imageChange(String id, String direction) {
		return service.getImage(id, direction);
	}

	public void displayChange(String id, String direction) {
		ImageObject updatedImage = imageChange(id, direction);
		viewController.updateDisplay(updatedImage);
	}

	public void pickMenuChange(boolean available, Integer inventoryLimit, HashMap<String, Item> playerContents, HashMap<String, Item> surfaceContents) {
		if (available) {
			viewController.updateMenu(inventoryLimit, playerContents, surfaceContents);
		} else {
			viewController.updateMenu(inventoryLimit, playerContents, null);
		}
	}

	public void surfaceChange(TreeMap<String, String> choices, ArrayList<String> availableSides) {

		pickMenuChange(model.surfaceAvailable(), model.getInventoryLimit(), model.getContents("player"), model.getContents(""));
		inputInterfaceChange(choices, availableSides);
		displayChange(model.getAreaID(), model.getDirection("true"));
		viewController.updateText(model.getPlayerName(), model.getAreaID(), model.getAreaName(), model.getAreaText(), model.getDirection("true"));

	}

    public void stateChange(String id) {
		model.updateContainers(id);
    }

	public void inventoryChange() {
		viewController.updateMenu(model.getInventoryLimit(), model.getContents("player"), model.getContents(""));
	}

}
