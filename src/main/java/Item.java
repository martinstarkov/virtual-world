public class Item {
    DrawableItem item;
    Item(DrawableItem item) {
        this.item = item;
    }

    public DrawableItem getDrawableItem() {
        return item;
    }
    public String getId() {
        return getDrawableItem().getId();
    }
    public String getDisplayName() {
        return getDrawableItem().getDisplayName();
    }
}
