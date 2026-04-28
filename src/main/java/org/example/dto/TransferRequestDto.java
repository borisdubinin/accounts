package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferRequestDto {

    @NotBlank(message = "IBAN is required to perform the transfer")
    private String ibanFrom;

    @NotBlank(message = "IBAN is required to perform the transfer")
    private String ibanTo;

    @NotNull(message = "Sent amount is required to perform the transfer")
    @Positive(message = "Sent amount cannot be less or equals to 0")
    private BigDecimal sentAmount;
}