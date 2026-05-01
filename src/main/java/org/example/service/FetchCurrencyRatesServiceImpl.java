package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.client.NationalBankClient;
import org.example.converter.CurrencyRateConverter;
import org.example.dto.CurrencyRateResponseDto;
import org.example.model.AccountCurrency;
import org.example.model.CurrencyRate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FetchCurrencyRatesServiceImpl implements FetchCurrencyRatesService {

    private static final int DAILY_CURRENCY_RATE_PERIODICITY = 0;

    private final NationalBankClient nationalBankClient;
    private final CurrencyRateConverter currencyRateConverter;

    @Override
    public List<CurrencyRate> fetchDailyRates() {
        List<CurrencyRateResponseDto> currencyRateResponseDtos = nationalBankClient.getRates(DAILY_CURRENCY_RATE_PERIODICITY)
                .stream()
                .filter(dto -> Arrays.stream(AccountCurrency.values())
                                .anyMatch(a -> a.toString().equals(dto.getCurrency())))
                .toList();
        return currencyRateConverter.toModels(currencyRateResponseDtos);
    }
}