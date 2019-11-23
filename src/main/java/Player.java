public class Player implements Inventory {
    private Sector sector;
    private String name;
    private Direction direction;
    public Player(String name) {
        this.name = name;
    }
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


    // improve this somehow
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
}
