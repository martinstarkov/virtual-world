public interface Service {

    String serviceName = "genericService";
    String serviceName();
    ImageObject getImage(String id, String direction);
}
