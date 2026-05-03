package org.example.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class Transfer {

    private String ibanFrom;
    private BigDecimal senderBalance;
    private AccountCurrency senderCurrency;
    private BigDecimal sentAmount;
    private String ibanTo;
    private BigDecimal receiverBalance;
    private AccountCurrency receiverCurrency;
}