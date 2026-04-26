package org.example.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.entity.AccountStatus;

import java.math.BigDecimal;

@Setter
@Getter
public class AccountResponseDto {

    private Long id;
    private BigDecimal balance;
    private String currency;
    private AccountStatus status;
}
