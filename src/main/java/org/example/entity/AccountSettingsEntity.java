package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.example.model.AccountSettings;

import java.math.BigDecimal;

@Entity
@Table(name = "account_settings")
@Getter
@Setter
public class AccountSettingsEntity {

    public AccountSettingsEntity(AccountSettings settings) {
        this.monthlyLimit = settings.getMonthlyLimit();
        this.smsNotificationsEnabled = settings.getSmsNotificationsEnabled();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "monthly_limit")
    private BigDecimal monthlyLimit;

    @Column(name = "sms_notifications_enabled")
    private Boolean smsNotificationsEnabled;
}