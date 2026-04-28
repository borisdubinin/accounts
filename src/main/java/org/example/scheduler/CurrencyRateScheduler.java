package org.example.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.client.NbrbClient;
import org.example.converter.CurrencyRateConverter;
import org.example.dto.CurrencyRateDto;
import org.example.entity.CurrencyRateEntity;
import org.example.repository.CurrencyRateRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CurrencyRateScheduler {

    private final CurrencyRateRepository currencyRateRepository;
    private final NbrbClient nbrbClient;
    private final CurrencyRateConverter currencyRateConverter;

    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Minsk")
    public void fetchAndSaveRates() {
        log.info("Daily fetching currency rates started");
        List<CurrencyRateDto> currencyRateDtos = nbrbClient.getDailyRates(0)
                .stream()
                .filter(dto -> "USD".equals(dto.getCurAbbreviation())
                        || "EUR".equals(dto.getCurAbbreviation()))
                .toList();
        List<CurrencyRateEntity> currencyRateEntities = currencyRateConverter.toEntities(currencyRateDtos);
        currencyRateRepository.saveAll(currencyRateEntities);
    }
}