package org.example.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "security")
public record SecurityProperties(Map<String, UserConfig> users) {

    public record UserConfig(String username, String password, String role) {
    }
}