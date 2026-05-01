package org.example.service;

import org.example.model.CurrencyRate;

import java.util.List;

public interface FetchCurrencyRatesService {

    /**
     * Fetches today currency rates via API
     */
    List<CurrencyRate> fetchDailyRates();
}