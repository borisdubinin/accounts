package org.example.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.FetchCurrencyRatesService;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CurrencyRateScheduler {

    private final FetchCurrencyRatesService fetchCurrencyRatesService;

    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Minsk")
    @Retryable
    public void fetchAndSaveDailyRates() {
        try {
            log.info("Attempt to fetch currency rates");
            fetchCurrencyRatesService.fetchAndSaveDailyRates();
            log.info("Fetched successfully");
        } catch(Exception e) {
            log.error("Failed to fetch currency rates: %s".formatted(e.getMessage()));
        }
    }
}