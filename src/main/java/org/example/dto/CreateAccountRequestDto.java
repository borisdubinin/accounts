package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateAccountRequestDto {

    @NotNull
    @PositiveOrZero
    private BigDecimal balance;

    @NotBlank
    @Size(min = 3, max = 3)
    private String currency;
}
