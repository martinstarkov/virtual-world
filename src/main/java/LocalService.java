import java.net.URL;

public class LocalService implements Service {

    private String serviceName = "localService";

    public LocalService() {}

    public String serviceName() {
        return this.serviceName;
    }

    public ImageObject getImage(String id, String direction) {

        String imageName = id + "-" + direction;
        // this means the id is that of an item, therefore do not add the dash "-" formatting
        // I did not have enough time to make id be the path of the file
        if (direction == "") {
            imageName = id;
        }
        // at the moment only supports JPG / PNG files
        String path = imageName + ".JPG";
        URL url = this.getClass().getResource(path);
        if (url == null) {
            // if URL was not found for ".JPG", try ".png"
            path = imageName + ".png";
            url = this.getClass().getResource(path);
        }
        try {
            // if no image found, return the default one
            if (url == null) {
                return new ImageObject("default.png");
            }
            return new ImageObject(path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
