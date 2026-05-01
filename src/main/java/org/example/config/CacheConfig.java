package org.example.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.example.config.properties.CacheProperties;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
@EnableConfigurationProperties(CacheProperties.class)
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(CacheProperties properties) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        properties.configs().forEach((name, config) -> {
            Cache<Object, Object> cache = buildCache(config);

            cacheManager.registerCustomCache(name, cache);
        });
        cacheManager.setCacheNames(properties.configs().keySet());
        cacheManager.setAllowNullValues(false);
        return cacheManager;
    }

    private @NonNull Cache<Object, Object> buildCache(CacheProperties.CacheConfig config) {
        return Caffeine.newBuilder()
                .expireAfterWrite(config.ttl(), TimeUnit.SECONDS)
                .maximumSize(config.maximumSize())
                .build();
    }
}
