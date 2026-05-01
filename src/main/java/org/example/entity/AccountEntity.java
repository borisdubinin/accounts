package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.model.AccountCurrency;
import org.example.model.AccountStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Getter
@Setter
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private AccountCurrency currency;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    private String iban;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, optional = false)
    private AccountSettingsEntity settings;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
