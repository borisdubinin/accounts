package org.example.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Account {

    private Long id;
    private BigDecimal balance;
    private AccountCurrency currency;
    private AccountStatus status;
    private String iban;
}
