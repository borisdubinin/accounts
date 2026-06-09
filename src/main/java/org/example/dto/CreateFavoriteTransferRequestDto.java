package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateFavoriteTransferRequestDto {

    @NotBlank
    @Size(min = 32, max = 32)
    private String ibanFrom;

    @NotBlank
    @Size(min = 32, max = 32)
    private String ibanTo;

    @NotNull
    @Positive
    private BigDecimal amount;
}
