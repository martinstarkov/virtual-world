import java.util.HashMap;

public class Player implements Inventory {

    private Sector sector = new Sector("", "", "");
    private String name = "";
    //must be a perfect square
    private Integer inventoryLimit = 9;
    private Direction direction = null;
    private HashMap<String, Item> contents = new HashMap<String, Item>();

    public Player(String name, Integer inventoryLimit) {
        this.name = name;
        this.inventoryLimit = inventoryLimit;
    }

    public Integer getInventoryLimit() { return inventoryLimit; }
    public Sector getSector() {
        return sector;
    }
    public void setSector(Sector sector) {
        this.sector = sector;
    }
    public String getName() {
        return name;
    }
    public Direction getDirection() {
        return direction;
    }

    // this returns the direction on a specific side relative to the player's current direction
    public Direction getSideDirection(String side) {
        // dirFactor is how far away a given relative direction is from the starting direction, eg "North, East, West", starting from North, West is +2, therefore dirFactor = 2
        Integer dirFactor;
        switch (side) {
            case "right":
                dirFactor = 1;
                break;
            case "back":
                dirFactor = 2;
                break;
            case "left":
                dirFactor = 3;
                break;
            default:
                return null;
        }
        return Direction.values()[(direction.ordinal() + dirFactor) % Direction.values().length];
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void addToInventory(Item item) {
        contents.put(item.getId(), item);
    }

    @Override
    public void removeFromInventory(Item item) {
        contents.remove(item.getId());
    }

    @Override
    public HashMap<String, Item> getContents() {
        return contents;
    }
}
