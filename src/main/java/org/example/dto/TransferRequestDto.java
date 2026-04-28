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

    @NotBlank
    private String ibanFrom;

    @NotBlank
    private String ibanTo;

    @NotNull
    @Positive
    private BigDecimal sentAmount;
}