package org.example.model;

import lombok.Getter;
import lombok.Setter;
import org.example.entity.AccountStatus;

import java.math.BigDecimal;
import java.util.Currency;

@Getter
@Setter
public class Account {

    private Long id;
    private BigDecimal balance;
    private Currency currency;
    private AccountStatus status;
}
