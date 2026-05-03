package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class AccountSettingsDto {

    @Schema(description = "Monthly spending limit for the account",
            example = "5000.00",
            defaultValue = "null"
    )
    private BigDecimal monthlyLimit;

    @Schema(description = "Whether SMS notifications are enabled for this account",
            example = "true",
            defaultValue = "false"
    )
    private Boolean smsNotificationsEnabled;
}