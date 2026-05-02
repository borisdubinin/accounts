package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.CurrencyRate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FetchCurrencyRatesService {

    private final NationalBankService nationalBankService;
    private final CurrencyRateService currencyRateService;

    public void fetchAndSaveDailyRates() {
        List<CurrencyRate> currencyRates = nationalBankService.fetchDailyRates();
        currencyRateService.saveAll(currencyRates);
    }
}