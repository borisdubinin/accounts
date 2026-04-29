package org.example.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cache.balance")
public record BalanceCacheProperties(int ttl, long maximumSize) {
}
