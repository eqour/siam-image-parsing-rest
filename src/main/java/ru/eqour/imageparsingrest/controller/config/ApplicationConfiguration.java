package ru.eqour.imageparsingrest.controller.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.eqour.imageparsingrest.serialization.ImageDeserializer;
import ru.eqour.imageparsingrest.serialization.ImageSerializer;

import java.awt.image.BufferedImage;
import java.time.Duration;

@Configuration
public class ApplicationConfiguration {

   @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(BufferedImage.class, new ImageSerializer());
        module.addDeserializer(BufferedImage.class, new ImageDeserializer());
        mapper.registerModule(module);
        return mapper;
    }

    @Bean
    public CacheManager ehCacheManager() {
        return CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("imageData",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, BufferedImage.class,
                                ResourcePoolsBuilder.newResourcePoolsBuilder()
                                        .heap(100, MemoryUnit.MB)
                        ).withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofMinutes(10)))
                ).build(true);
    }
}
