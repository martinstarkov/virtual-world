import javafx.scene.image.Image;
/*
import java.util.HashMap;

class CachedImage {
    HashMap<Integer, ImageObject> indexCache = new HashMap<Integer, ImageObject>();
    CachedImage(Service service, Sector sector, int index) {
        ImageObject image = service.getImage(sector.getId());
        addToIndexCache(image, index);
    }
    public void addToIndexCache(ImageObject image, int index) {
        this.indexCache.put(index, image);
    }
    public HashMap<Integer, ImageObject> getIndexCache() {
        return this.indexCache;
    }
}

public class CacheProxy {
    HashMap<String, CachedImage> sectorCache = new HashMap<>();
    public void addToCache(Service service, Sector sector, int index) {
        CachedImage imageCache = new CachedImage(service, sector, index);
        this.sectorCache.put(sector.getId(), imageCache);
    }
    public boolean sectorInCache(Sector sector) {
        return sectorCache.containsKey(sector.getId());
    }
    public boolean indexInCache(Sector sector, Integer index) {
        // check if sector exists in cache, AND if that sector's specific index of image exists in the cache.
        return sectorCache.containsKey(sector.getId()) && sectorCache.get(sector.getId()).getIndexCache().containsKey(index);
    }
}
*/