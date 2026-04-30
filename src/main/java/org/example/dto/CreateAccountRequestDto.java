package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import org.example.model.AccountCurrency;
import org.example.model.AccountSettings;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(description = "Request to create a new account")
public class CreateAccountRequestDto {

    @Schema(
            description = "Initial account balance",
            example = "1234.00",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Initial account balance is required")
    @PositiveOrZero(message = "Balance cannot be less than 0")
    private BigDecimal balance;

    @Schema(
            description = "Account currency (ISO 4217 code)",
            allowableValues = { "BYN", "RUB", "USD", "EUR" },
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Account currency is required")
    private AccountCurrency currency;

    @Schema(
            description = "Account settings",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private AccountSettings settings;
}
