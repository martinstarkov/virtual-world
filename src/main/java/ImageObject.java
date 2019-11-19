import javafx.scene.image.Image;

import java.io.File;

public class ImageObject {
    private Image javaFxImage;
    private String path;
    private File file;
    ImageObject(File file) {
        this.file = file;
        this.javaFxImage = new Image(this.file.toURI().toString());
    }
    public String getPath() {
        return path;
    }
    public Image getJavaFxImage() {
        return javaFxImage;
    }
}