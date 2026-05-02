package org.example.service;

import org.example.client.NationalBankClient;
import org.example.converter.CurrencyModelAndDtoConverter;
import org.example.dto.CurrencyRateResponseDto;
import org.example.model.AccountCurrency;
import org.example.model.CurrencyRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FetchCurrencyRatesServiceImplTest {

    @Mock
    private NationalBankClient nationalBankClient;

    @Spy
    private CurrencyModelAndDtoConverter currencyRateConverter;

    @InjectMocks
    private FetchCurrencyRatesServiceImpl fetchCurrencyRatesService;

    private CurrencyRateResponseDto usdDto;
    private CurrencyRateResponseDto eurDto;
    private CurrencyRateResponseDto rubDto;
    private CurrencyRateResponseDto gbpDto;

    @BeforeEach
    void setUp() {
        usdDto = createDto("USD", 1, new BigDecimal("3.03"));
        eurDto = createDto("EUR", 1, new BigDecimal("3.5"));
        rubDto = createDto("RUB", 100, new BigDecimal("3.0"));
        gbpDto = createDto("GBP", 1, new BigDecimal("4.0"));
    }

    private CurrencyRateResponseDto createDto(String currency, Integer scale, BigDecimal rate) {
        CurrencyRateResponseDto dto = new CurrencyRateResponseDto();
        dto.setDate(LocalDate.now());
        dto.setCurrency(currency);
        dto.setScale(scale);
        dto.setRate(rate);
        return dto;
    }

    @Test
    void fetchDailyRates_ShouldFilterAndConvertCorrectly() {
        List<CurrencyRateResponseDto> allRates = List.of(usdDto, eurDto, rubDto, gbpDto);
        when(nationalBankClient.getRates(0)).thenReturn(allRates);

        List<CurrencyRate> result = fetchCurrencyRatesService.fetchDailyRates();

        validateSavedRates(result);
    }

    private void validateSavedRates(List<CurrencyRate> rates) {
        assertThat(rates).hasSize(3);

        assertThat(rates).anySatisfy(rate -> {
            assertThat(rate.getCurrency()).isEqualTo(AccountCurrency.USD);
            assertThat(rate.getScale()).isEqualTo(1);
            assertThat(rate.getRate()).isEqualByComparingTo("3.03");
        });

        assertThat(rates).anySatisfy(rate -> {
            assertThat(rate.getCurrency()).isEqualTo(AccountCurrency.EUR);
            assertThat(rate.getScale()).isEqualTo(1);
            assertThat(rate.getRate()).isEqualByComparingTo("3.5");
        });

        assertThat(rates).anySatisfy(rate -> {
            assertThat(rate.getCurrency()).isEqualTo(AccountCurrency.RUB);
            assertThat(rate.getScale()).isEqualTo(100);
            assertThat(rate.getRate()).isEqualByComparingTo("3.0");
        });
    }

    @Test
    void fetchDailyRates_WhenNoSupportedCurrencies_ShouldSaveEmptyList() {
        when(nationalBankClient.getRates(0)).thenReturn(List.of(gbpDto));

        List<CurrencyRate> result = fetchCurrencyRatesService.fetchDailyRates();

        assertThat(result).hasSize(0);
    }

    @Test
    void fetchDailyRates_WhenClientReturnsEmptyList_ShouldSaveEmptyList() {
        when(nationalBankClient.getRates(0)).thenReturn(List.of());

        List<CurrencyRate> result = fetchCurrencyRatesService.fetchDailyRates();

        assertThat(result).hasSize(0);
    }
}