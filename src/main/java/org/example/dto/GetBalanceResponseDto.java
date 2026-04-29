package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.example.model.AccountCurrency;

import java.math.BigDecimal;

@Getter
@Setter
public class GetBalanceResponseDto {

    @Schema(description = "Current balance of the account", example = "1234.00")
    private BigDecimal balance;

    @Schema(description = "Account currency (ISO 4217 code)")
    private AccountCurrency currency;
}
