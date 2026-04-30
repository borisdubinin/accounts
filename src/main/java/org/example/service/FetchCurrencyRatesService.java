package org.example.service;

public interface FetchCurrencyRatesService {

    /**
     * Fetches today currency rates via API and saves them to the repository
     */
    void fetchAndSaveDailyRates();
}