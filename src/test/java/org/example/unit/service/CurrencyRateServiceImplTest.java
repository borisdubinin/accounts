package org.example.unit.service;

import org.example.converter.CurrencyEntityAndModelConverter;
import org.example.entity.CurrencyRateEntity;
import org.example.exception.EntityNotFoundException;
import org.example.model.AccountCurrency;
import org.example.model.CurrencyRate;
import org.example.repository.CurrencyRateRepository;
import org.example.service.CurrencyRateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyRateServiceImplTest {

    @Mock
    private CurrencyRateRepository currencyRateRepository;

    @Spy
    private CurrencyEntityAndModelConverter currencyRateConverter;

    @InjectMocks
    private CurrencyRateServiceImpl currencyRateServiceImpl;

    private CurrencyRateEntity testEntity;
    private CurrencyRate testModel;
    private LocalDate today;

    @BeforeEach
    void setUp() {
        today = LocalDate.now();

        testEntity = new CurrencyRateEntity();
        testEntity.setId(1L);
        testEntity.setCurrency(AccountCurrency.USD);
        testEntity.setRate(BigDecimal.valueOf(3.03));
        testEntity.setScale(1);
        testEntity.setDate(today);

        testModel = new CurrencyRate();
        testModel.setCurrency(AccountCurrency.USD);
        testModel.setRate(BigDecimal.valueOf(3.03));
        testModel.setScale(1);
        testModel.setDate(today);
    }

    @Nested
    class GetTodayRateTests {

        @Test
        void getTodayRate_ShouldReturnRate_WhenExists() {
            AccountCurrency currency = AccountCurrency.USD;
            testModel.setCurrency(currency);
            when(currencyRateRepository.getByCurrencyAndDate(currency, today))
                    .thenReturn(Optional.of(testEntity));

            CurrencyRate result = currencyRateServiceImpl.getTodayRate(currency);

            assertThat(result).usingRecursiveComparison().isEqualTo(testModel);
            verify(currencyRateRepository).getByCurrencyAndDate(currency, today);
        }

        @Test
        void getTodayRate_ShouldThrowEntityNotFoundException_WhenRateDoesNotExist() {
            AccountCurrency currency = AccountCurrency.EUR;
            when(currencyRateRepository.getByCurrencyAndDate(currency, today))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> currencyRateServiceImpl.getTodayRate(currency))
                    .isInstanceOf(EntityNotFoundException.class);
        }

        @Test
        void getTodayRate_ShouldHandleMultipleCurrencies() {
            AccountCurrency usd = AccountCurrency.USD;
            AccountCurrency eur = AccountCurrency.EUR;

            CurrencyRateEntity usdEntity = testEntity;
            CurrencyRateEntity eurEntity = new CurrencyRateEntity();
            eurEntity.setCurrency(AccountCurrency.EUR);
            eurEntity.setRate(BigDecimal.valueOf(3.5));

            CurrencyRate usdModel = testModel;
            CurrencyRate eurModel = new CurrencyRate();
            eurModel.setCurrency(AccountCurrency.EUR);
            eurModel.setRate(BigDecimal.valueOf(3.5));

            when(currencyRateRepository.getByCurrencyAndDate(usd, today))
                    .thenReturn(Optional.of(usdEntity));
            when(currencyRateRepository.getByCurrencyAndDate(eur, today))
                    .thenReturn(Optional.of(eurEntity));

            assertThat(currencyRateServiceImpl.getTodayRate(usd)).usingRecursiveComparison().isEqualTo(usdModel);
            assertThat(currencyRateServiceImpl.getTodayRate(eur)).usingRecursiveComparison().isEqualTo(eurModel);
        }
    }

    @Nested
    class SaveAllTests {

        @Test
        void saveAll_ShouldSaveAllCurrencyRates_WhenListIsValid() {
            List<CurrencyRate> rates = List.of(testModel);

            currencyRateServiceImpl.saveAll(rates);

            verify(currencyRateConverter).toEntities(rates);
            verify(currencyRateRepository).saveAll(any());
        }

        @Test
        void saveAll_ShouldHandleEmptyList() {
            List<CurrencyRate> rates = List.of();

            currencyRateServiceImpl.saveAll(rates);

            verify(currencyRateRepository).saveAll(List.of());
        }

        @Test
        void saveAll_ShouldHandleLargeList() {
            List<CurrencyRate> rates = List.of(
                    testModel,
                    new CurrencyRate(),
                    new CurrencyRate()
            );

            currencyRateServiceImpl.saveAll(rates);

            verify(currencyRateRepository).saveAll(any());
        }
    }
}