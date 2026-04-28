package org.example.client;

import org.example.dto.CurrencyRateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "nbrbClient", url = "https://api.nbrb.by")
public interface NbrbClient {

    @GetMapping("/exrates/rates")
    List<CurrencyRateDto> getDailyRates(@RequestParam("periodicity") int periodicity);
}