package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferRequestDto {

    @Schema(
            description = "IBAN of the account from which money will be sent",
            example = "IBANQEDESSDGPEB3FR9H8QZ2MOT1S58A",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "IBAN is required to perform the transfer")
    @Size(min = 32, max = 32)
    private String ibanFrom;

    @Schema(
            description = "IBAN of the account to which money will be sent",
            example = "IBANQEDESSDGPEB3FR9H8QZ2MOT1S58A",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "IBAN is required to perform the transfer")
    @Size(min = 32, max = 32)
    private String ibanTo;

    @Schema(
            description = "Amount of money which will be sent",
            example = "1234.00",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Sent amount is required to perform the transfer")
    @Positive(message = "Sent amount cannot be less or equals to 0")
    @Digits(integer = 18, fraction = 2, message = "The amount must contain no more than 2 decimal places")
    private BigDecimal sentAmount;
}