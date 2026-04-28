package org.example.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.model.AccountCurrency;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferResponseDto {

    private String ibanFrom;
    private BigDecimal senderBalance;
    private AccountCurrency senderCurrency;
    private BigDecimal amount;
    private String ibanTo;
    private BigDecimal receiverBalance;
    private AccountCurrency receiverCurrency;
}