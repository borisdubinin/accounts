package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.client.NationalBankClient;
import org.example.converter.CurrencyRateConverter;
import org.example.dto.CurrencyRateResponseDto;
import org.example.entity.CurrencyRateEntity;
import org.example.exception.EntityNotFoundException;
import org.example.model.AccountCurrency;
import org.example.model.CurrencyRate;
import org.example.repository.CurrencyRateRepository;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyRateServiceImpl implements CurrencyRateService {

    private final NationalBankClient nationalBankClient;
    private final CurrencyRateRepository currencyRateRepository;
    private final CurrencyRateConverter currencyRateConverter;

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

    @Override
    @Retryable(maxRetries = 3)
    public void fetchAndSaveDailyRates() {
        List<CurrencyRateResponseDto> currencyRateResponseDtos = nationalBankClient.getRates(0)
                .stream()
                .filter(dto ->
                        Arrays.stream(AccountCurrency.values())
                                .anyMatch(a -> a.toString().equals(dto.getCurrency()))
                ).toList();
        List<CurrencyRate> currencyRates = currencyRateConverter.toModels(currencyRateResponseDtos);
        saveAll(currencyRates);
    }
}