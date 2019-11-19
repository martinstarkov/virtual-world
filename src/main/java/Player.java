public class Player implements Inventory {
    Sector sector;
    String name;
    Direction direction;
    Player(String name) {
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
    public Direction getSideDirection(String side) {
        Integer addToDirection = 0;
        if (side == "left") {
            addToDirection = 3;
        } else if (side == "right") {
            addToDirection = 1;
        } else if (side == "back") {
            addToDirection = 2;
        }
        Integer directionIndex = direction.ordinal();
        return Direction.values()[(directionIndex + addToDirection) % Direction.values().length];

    }
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
