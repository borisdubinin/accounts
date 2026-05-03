package org.example.converter;

import org.example.entity.CurrencyRateEntity;
import org.example.model.CurrencyRate;
import org.springframework.stereotype.Component;

@Component
public class CurrencyEntityAndModelConverter implements
        EntityToModelConverter<CurrencyRateEntity, CurrencyRate>,
        ModelToEntityConverter<CurrencyRate, CurrencyRateEntity> {

    @Override
    public CurrencyRate toModel(CurrencyRateEntity entity) {
        CurrencyRate model = new CurrencyRate();
        model.setDate(entity.getDate());
        model.setCurrency(entity.getCurrency());
        model.setScale(entity.getScale());
        model.setRate(entity.getRate());
        return model;
    }

    @Override
    public CurrencyRateEntity toEntity(CurrencyRate model) {
        CurrencyRateEntity entity = new CurrencyRateEntity();
        entity.setDate(model.getDate());
        entity.setCurrency(model.getCurrency());
        entity.setScale(model.getScale());
        entity.setRate(model.getRate());
        return entity;
    }
}