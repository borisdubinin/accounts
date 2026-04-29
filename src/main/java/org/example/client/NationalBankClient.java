package org.example.client;

import org.example.dto.CurrencyRateResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "national-bank-client", url = "${national-bank-client.url}")
public interface NationalBankClient {

    @GetMapping("${national-bank-client.currency-rates-endpoint}")
    List<CurrencyRateResponseDto> getDailyRates(@RequestParam int periodicity);
}