package org.example.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.client.NbrbClient;
import org.example.converter.CurrencyRateConverter;
import org.example.dto.CurrencyRateDto;
import org.example.model.AccountCurrency;
import org.example.model.CurrencyRate;
import org.example.service.CurrencyRateService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CurrencyRateScheduler {

    private final CurrencyRateService currencyRateService;
    private final NbrbClient nbrbClient;
    private final CurrencyRateConverter currencyRateConverter;

    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Minsk")
    public void fetchAndSaveRates() {
        log.info("Daily fetching currency rates started");
        List<CurrencyRateDto> currencyRateDtos = nbrbClient.getDailyRates(0)
                .stream()
                .filter(dto ->
                        Arrays.stream(AccountCurrency.values())
                                .anyMatch(a -> a.toString().equals(dto.getCurAbbreviation()))
                ).toList();
        List<CurrencyRate> currencyRates = currencyRateConverter.toModels(currencyRateDtos);
        currencyRateService.saveAll(currencyRates);
    }
}