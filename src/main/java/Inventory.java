import java.util.HashMap;

public interface Inventory {

    void addToInventory(Item item);
    void removeFromInventory(Item item);
    HashMap<String, Item> getContents();
}
