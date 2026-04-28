package org.example.converter;

import org.example.dto.CurrencyRateDto;
import org.example.entity.CurrencyRateEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CurrencyRateConverter {

    public CurrencyRateEntity toEntity(CurrencyRateDto dto) {
        CurrencyRateEntity entity = new CurrencyRateEntity();
        entity.setDate(dto.getDate());
        entity.setCurrency(dto.getCurAbbreviation());
        entity.setRate(dto.getCurOfficialRate());
        return entity;
    }

    public List<CurrencyRateEntity> toEntities(List<CurrencyRateDto> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .toList();
    }
}