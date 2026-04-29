package org.example.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.example.config.properties.BalanceCacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
@EnableConfigurationProperties(BalanceCacheProperties.class)
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(BalanceCacheProperties properties) {
        var cacheManager = new CaffeineCacheManager("balance");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(properties.ttl(), TimeUnit.SECONDS)
                .maximumSize(properties.maximumSize()));
        return cacheManager;
    }
}
