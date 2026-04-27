package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.example.entity.AccountStatus;

import java.math.BigDecimal;

@Setter
@Getter
@Schema(description = "The result of a successful request containing the account data")
public class AccountResponseDto {

    @Schema(description = "Unique account identifier", example = "1001")
    private Long id;

    @Schema(description = "Balance of the account", example = "1234.00")
    private BigDecimal balance;

    @Schema(description = "Account currency (ISO 4217 code)", examples = { "BYN", "RUB", "USD" })
    private String currency;

    @Schema(description = "Current status of the account")
    private AccountStatus status;
}
