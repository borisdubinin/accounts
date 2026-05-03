package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.converter.CurrencyEntityAndModelConverter;
import org.example.entity.CurrencyRateEntity;
import org.example.exception.EntityNotFoundException;
import org.example.model.AccountCurrency;
import org.example.model.CurrencyRate;
import org.example.repository.CurrencyRateRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyRateServiceImpl implements CurrencyRateService {

    private final CurrencyRateRepository currencyRateRepository;
    private final CurrencyEntityAndModelConverter currencyRateConverter;

    @Override
    public CurrencyRate getTodayRate(AccountCurrency currency) {
        CurrencyRateEntity entity = currencyRateRepository.getByCurrencyAndDate(currency, LocalDate.now())
                .orElseThrow(() -> new EntityNotFoundException("Currency rate not found: %s".formatted(currency.toString())));
        return currencyRateConverter.toModel(entity);
    }

    @Override
    public void saveAll(List<CurrencyRate> currencyRates) {
        List<CurrencyRateEntity> entities = currencyRateConverter.toEntities(currencyRates);
        currencyRateRepository.saveAll(entities);
    }
}