import javafx.scene.image.Image;

// This object allows for other libraries' image objects to be used (other than JavaFX) without having to modify code in many places

public class ImageObject {

    private Image javaFxImage;
    private String path;
    ImageObject(String path) {
        this.path = path;
        this.javaFxImage = new Image(path);
        // to add new library supplied image object, simply define the object here
    }
    // add the new image object to this function's return
    public Image getImage() {
        return javaFxImage;
    }
    public String getPath() { return path; }
}