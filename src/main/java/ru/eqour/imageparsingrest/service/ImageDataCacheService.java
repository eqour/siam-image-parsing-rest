package ru.eqour.imageparsingrest.service;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.eqour.imageparsingrest.exception.NotFoundCacheDataException;

import java.awt.image.BufferedImage;

@Service
public class ImageDataCacheService {

    private final CacheManager ehCacheManager;

    @Autowired
    public ImageDataCacheService(CacheManager ehCacheManager) {
        this.ehCacheManager = ehCacheManager;
    }

    public void saveImage(Long id, BufferedImage image) {
        ehCacheManager.getCache("imageData", Long.class, BufferedImage.class).put(id, image);
    }

    public BufferedImage getImageById(Long id) {
        Cache<Long, BufferedImage> cache = ehCacheManager.getCache("imageData", Long.class, BufferedImage.class);
        if (cache.containsKey(id)) {
            return cache.get(id);
        } else {
            throw new NotFoundCacheDataException("Изображение отсутствует в кэше");
        }
    }
}
