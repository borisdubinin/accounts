package org.example.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.entity.AccountSettingsEntity;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class AccountSettings {

    public AccountSettings(AccountSettingsEntity settingsEntity) {
        this.monthlyLimit = settingsEntity.getMonthlyLimit();
        this.smsNotificationsEnabled = settingsEntity.getSmsNotificationsEnabled();
    }

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