import javafx.scene.image.Image;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;

public class LocalService implements Service {
    String serviceName = "localService";
    String imageDirectory;
    LocalService(String imageDirectory) {
        this.imageDirectory = imageDirectory;
    }
    public String serviceName() {
        return this.serviceName;
    }

    public ImageObject getImage(String id, Direction direction) {
        File folder = new File (imageDirectory);
        String imageName = id + "-" + direction.toString().toLowerCase();
        File[] matchingFiles = folder.listFiles((dir, name) -> {
            // look for files that start with the sector name and with in an acceptable image format (png / jpg)
            return name.startsWith(imageName) && (name.endsWith("png") || name.endsWith("jpg"));
        });
            // look for files that start with the sector name and with in an acceptable image format (png / jpg)
        File[] defaultFile = folder.listFiles((dir, name) -> {
            // look for files that start with the sector name and with in an acceptable image format (png / jpg)
            return name.startsWith("default") && (name.endsWith("png") || name.endsWith("jpg"));
        });
        if (matchingFiles != null) {
            if (matchingFiles.length > 0) {
                File file = matchingFiles[0].getAbsoluteFile();
                ImageObject image = new ImageObject(file);
                return image;
            }
        }
        if (defaultFile != null) {
            if (defaultFile.length > 0) {
                return new ImageObject(defaultFile[0]);
            }
        }
        return null;
    }
}
