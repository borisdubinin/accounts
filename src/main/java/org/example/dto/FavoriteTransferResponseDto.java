package org.example.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class FavoriteTransferResponseDto {

    private Long id;
    String ibanFrom;
    String ibanTo;
    BigDecimal amount;
}
