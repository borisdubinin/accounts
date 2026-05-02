package org.example.converter;

import org.example.dto.CurrencyRateResponseDto;
import org.example.model.AccountCurrency;
import org.example.model.CurrencyRate;
import org.springframework.stereotype.Component;

@Component
public class CurrencyModelAndDtoConverter implements
        DtoToModelConverter<CurrencyRateResponseDto, CurrencyRate> {

    public CurrencyRate toModel(CurrencyRateResponseDto dto) {
        CurrencyRate model = new CurrencyRate();
        model.setDate(dto.getDate());
        try {
            model.setCurrency(AccountCurrency.valueOf(dto.getCurrency()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("%s isn't allowed currency ISO 4217 code".formatted(dto.getCurrency()));
        }
        model.setScale(dto.getScale());
        model.setRate(dto.getRate());
        return model;
    }
}