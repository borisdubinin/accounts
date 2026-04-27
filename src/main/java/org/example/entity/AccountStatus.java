package org.example.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Account status")
public enum AccountStatus {

    @Schema(description = "Account is active and operational")
    ACTIVE,

    @Schema(description = "Account is inactive and inoperational")
    BLOCKED
}
