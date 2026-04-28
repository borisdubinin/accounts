package org.example.converter;

import org.example.dto.CurrencyRateDto;
import org.example.entity.CurrencyRateEntity;
import org.example.model.AccountCurrency;
import org.example.model.CurrencyRate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CurrencyRateConverter {

    public CurrencyRateEntity toEntity(CurrencyRate model) {
        CurrencyRateEntity entity = new CurrencyRateEntity();
        entity.setDate(model.getDate());
        entity.setCurrency(model.getCurrency());
        entity.setScale(model.getScale());
        entity.setRate(model.getRate());
        return entity;
    }

    public List<CurrencyRateEntity> toEntities(List<CurrencyRate> models) {
        return models.stream()
                .map(this::toEntity)
                .toList();
    }

    public CurrencyRate toModel(CurrencyRateEntity entity) {
        CurrencyRate model = new CurrencyRate();
        model.setDate(entity.getDate());
        model.setCurrency(entity.getCurrency());
        model.setScale(entity.getScale());
        model.setRate(entity.getRate());
        return model;
    }

    public CurrencyRate toModel(CurrencyRateDto dto) {
        CurrencyRate model = new CurrencyRate();
        model.setDate(dto.getDate());
        try {
            model.setCurrency(AccountCurrency.valueOf(dto.getCurAbbreviation()));
        } catch(IllegalArgumentException e) {
            throw new IllegalArgumentException("%s isn't allowed currency ISO 4217 code".formatted(dto.getCurAbbreviation()));
        }
        model.setScale(dto.getCurScale());
        model.setRate(dto.getCurOfficialRate());
        return model;
    }

    public List<CurrencyRate> toModels(List<CurrencyRateDto> dtos) {
        return dtos.stream()
                .map(this::toModel)
                .toList();
    }
}