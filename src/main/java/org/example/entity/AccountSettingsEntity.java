package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
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
    @Column(name = "account_id")
    private Long accountId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "account_id")
    private AccountEntity account;

    @Column(name = "monthly_limit")
    private BigDecimal monthlyLimit;

    @Column(name = "sms_notifications_enabled")
    private Boolean smsNotificationsEnabled;
}