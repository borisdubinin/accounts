package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "account_settings")
@Getter
@Setter
@NoArgsConstructor
public class AccountSettingsEntity {

    public AccountSettingsEntity(BigDecimal monthlyLimit, Boolean smsNotificationsEnabled) {
        this.monthlyLimit = monthlyLimit;
        this.smsNotificationsEnabled = smsNotificationsEnabled;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "monthly_limit")
    private BigDecimal monthlyLimit;

    @Column(name = "sms_notifications_enabled")
    private Boolean smsNotificationsEnabled;
}