package org.example.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class CurrencyRateDto {

    @JsonAlias(value = "Cur_ID")
    private Integer curId;

    @JsonAlias(value = "Date")
    private LocalDate date;

    @JsonAlias(value = "Cur_Abbreviation")
    private String curAbbreviation;

    @JsonAlias(value = "Cur_Scale")
    private Integer curScale;

    @JsonAlias(value = "Cur_Name")
    private String curName;

    @JsonAlias(value = "Cur_OfficialRate")
    private BigDecimal curOfficialRate;
}
