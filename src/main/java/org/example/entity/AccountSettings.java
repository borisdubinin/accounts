package org.example.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "account_settings")
@Getter
@Setter
public class AccountSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;

    @Schema(description = "Monthly spending limit for the account", example = "5000.00", defaultValue = "null")
    @Column(name = "monthly_limit")
    private BigDecimal monthlyLimit;

    @Schema(description = "Whether SMS notifications are enabled for this account", example = "true", defaultValue = "false")
    @Column(name = "sms_notifications_enabled")
    private Boolean smsNotificationsEnabled;
}