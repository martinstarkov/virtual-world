// My implementation of an item that can be drawn, has a unique ID defined in JSON and a display name.
// This could be switched out as long as the Item class gets updated with the new item implementation.

public class DrawableItem {

    private String id;
    private String displayName;

    public DrawableItem(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getId() {
        return id;
    }
}
