package org.example.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class CurrencyRate {

    private LocalDate date;
    private AccountCurrency currency;
    private Integer scale;
    private BigDecimal rate;
}