package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.model.AccountCurrency;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfers")
@Getter
@Setter
public class TransferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "iban_from")
    private String ibanFrom;

    @Column(name = "sender_balance")
    private BigDecimal senderBalance;

    @Enumerated(EnumType.STRING)
    @Column(name = "sender_currency")
    private AccountCurrency senderCurrency;

    private BigDecimal amount;

    @Column(name = "iban_to")
    private String ibanTo;

    @Enumerated(EnumType.STRING)
    @Column(name = "receiver_currency")
    private AccountCurrency receiverCurrency;

    @Column(name = "receiver_balance")
    private BigDecimal receiverBalance;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
