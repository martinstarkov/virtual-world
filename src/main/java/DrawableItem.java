public class DrawableItem {
    private String id;
    private String displayName;
    private String path;
    public DrawableItem(String id, String displayName, String path) {
        this.id = id;
        this.displayName = displayName;
        this.path = path;
    }
    public String getDisplayName() {
        return displayName;
    }

    public String getId() {
        return id;
    }
    public String getPath() {
        return path;
    }

}
