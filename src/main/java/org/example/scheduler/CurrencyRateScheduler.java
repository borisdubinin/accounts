package org.example.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.CurrencyRateService;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class CurrencyRateScheduler {

    private final CurrencyRateService currencyRateService;

    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Minsk")
    @Retryable
    public void fetchAndSaveDailyRates() {
        try {
            log.info("Attempt to fetch currency rates (%s)".formatted(LocalDate.now()));
            currencyRateService.fetchAndSaveDailyRates();
            log.info("Currency rates (%s) fetched successfully".formatted(LocalDate.now()));
        } catch(Exception e) {
            log.error("Failed to fetch currency rates (%s): %s".
                    formatted(LocalDate.now(), e.getMessage()));
        }
    }
}