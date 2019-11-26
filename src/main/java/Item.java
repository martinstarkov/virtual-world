
// This general Item allows for more Items implementations to be written and used in the code (similar to ImageObject)

public class Item {

    DrawableItem drawableItem;
    Item(DrawableItem item) {
        this.drawableItem = item;
        // simply add new item implementation here
    }
    // return new item implementation from this function
    public DrawableItem getItem() {
        return drawableItem;
    }
    public String getId() {
        return getItem().getId();
    }
    public String getDisplayName() {
        return getItem().getDisplayName();
    }
}
