package org.example.config.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class SecurityProperties {

    @Value("${security.users.admin.username}")
    private String adminUsername;

    @Value("${security.users.admin.password}")
    private String adminPassword;

    @Value("${security.users.user.username}")
    private String userUsername;

    @Value("${security.users.user.password}")
    private String userPassword;
}