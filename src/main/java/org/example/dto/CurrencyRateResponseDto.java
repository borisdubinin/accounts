package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class CurrencyRateResponseDto {

    @JsonProperty("Date")
    private LocalDate date;

    @JsonProperty("Cur_Abbreviation")
    private String currency;

    @JsonProperty("Cur_Scale")
    private Integer scale;

    @JsonProperty("Cur_OfficialRate")
    private BigDecimal rate;
}
