package org.example.service;

import org.example.model.AccountCurrency;
import org.example.model.CurrencyRate;

import java.util.List;

public interface CurrencyRateService {

    /**
     * @param currency enum {@link AccountCurrency} determining which currency rate you need to get
     * @return Today {@link CurrencyRate} for this currency
     */
    CurrencyRate getTodayRate(AccountCurrency currency);

    /**
     * Saves all currency rates to the repository
     * @param currencyRates list of {@link CurrencyRate} you need to save
     */
    void saveAll(List<CurrencyRate> currencyRates);
}