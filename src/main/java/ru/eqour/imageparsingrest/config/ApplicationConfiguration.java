package ru.eqour.imageparsingrest.config;

import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.image.BufferedImage;
import java.time.Duration;

@Configuration
public class ApplicationConfiguration {

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
