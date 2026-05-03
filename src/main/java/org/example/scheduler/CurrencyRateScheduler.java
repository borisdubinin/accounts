package org.example.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.FetchCurrencyRatesService;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class CurrencyRateScheduler {

    private final FetchCurrencyRatesService fetchCurrencyRatesService;

    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Minsk")
    @Retryable
    public void fetchAndSaveDailyRates() {
        log.info("Attempt to fetch currency rates (%s)".formatted(LocalDate.now()));
        fetchCurrencyRatesService.fetchAndSaveDailyRates();
        log.info("Currency rates (%s) fetched successfully".formatted(LocalDate.now()));
    }

    @Recover
    public void recoverFetchRates(Exception e) {
        log.error("Failed to fetch currency rates (%s): %s".formatted(LocalDate.now(), e.getMessage()));
    }
}