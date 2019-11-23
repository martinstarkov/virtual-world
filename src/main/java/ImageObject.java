import javafx.scene.image.Image;

public class ImageObject {
    private Image javaFxImage;
    private String path;
    ImageObject(String path) {
        this.path = path;
        this.javaFxImage = new Image(path);
    }
    public Image getJavaFxImage() {
        return javaFxImage;
    }
    public String getPath() { return path; }
}