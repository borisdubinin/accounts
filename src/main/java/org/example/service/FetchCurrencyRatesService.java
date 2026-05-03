package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.CurrencyRate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FetchCurrencyRatesService {

    private final NationalBankService nationalBankService;
    private final CurrencyRateService currencyRateService;

    public void fetchAndSaveDailyRates() {
        List<CurrencyRate> currencyRates = nationalBankService.fetchDailyRates();
        currencyRateService.saveAll(currencyRates);
    }
}