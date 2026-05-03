package org.example.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "cache")
public record CacheProperties(Map<String, CacheConfig> configs) {

    public record CacheConfig(int ttl, long maximumSize) {
    }
}