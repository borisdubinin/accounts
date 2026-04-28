package org.example.service;

import org.example.model.AccountCurrency;
import org.example.model.CurrencyRate;

import java.util.List;

public interface CurrencyRateService {

    CurrencyRate getTodayRate(AccountCurrency currency);

    void saveAll(List<CurrencyRate> currencyRates);
}