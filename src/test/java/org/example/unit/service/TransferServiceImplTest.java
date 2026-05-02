package org.example.unit.service;

import org.example.entity.AccountEntity;
import org.example.exception.EntityNotFoundException;
import org.example.model.AccountCurrency;
import org.example.model.AccountStatus;
import org.example.model.CurrencyRate;
import org.example.model.Transfer;
import org.example.repository.AccountRepository;
import org.example.service.CurrencyRateService;
import org.example.service.TransferServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CurrencyRateService currencyRateService;

    @InjectMocks
    private TransferServiceImpl transferService;

    private AccountEntity sender;
    private AccountEntity receiver;
    private Transfer transfer;
    private CurrencyRate usdRate;
    private CurrencyRate eurRate;

    @BeforeEach
    void setUp() {
        sender = new AccountEntity();
        sender.setId(1L);
        sender.setIban("IBAN1111111111111111111111111111");
        sender.setBalance(new BigDecimal("1000.00"));
        sender.setCurrency(AccountCurrency.USD);
        sender.setStatus(AccountStatus.ACTIVE);

        receiver = new AccountEntity();
        receiver.setId(2L);
        receiver.setIban("IBAN9999999999999999999999999999");
        receiver.setBalance(new BigDecimal("500.00"));
        receiver.setCurrency(AccountCurrency.EUR);
        receiver.setStatus(AccountStatus.ACTIVE);

        transfer = Transfer.builder()
                .ibanFrom("IBAN1111111111111111111111111111")
                .ibanTo("IBAN9999999999999999999999999999")
                .sentAmount(new BigDecimal("100.00"))
                .build();

        usdRate = new CurrencyRate();
        usdRate.setCurrency(AccountCurrency.USD);
        usdRate.setRate(new BigDecimal("3.03"));
        usdRate.setScale(1);

        eurRate = new CurrencyRate();
        eurRate.setCurrency(AccountCurrency.EUR);
        eurRate.setRate(new BigDecimal("3.5"));
        eurRate.setScale(1);
    }

    @Test
    void performTransfer_WhenSameCurrency_ShouldTransferCorrectly() {
        receiver.setCurrency(AccountCurrency.USD);
        when(accountRepository.findByIbanWithLock("IBAN1111111111111111111111111111")).thenReturn(Optional.of(sender));
        when(accountRepository.findByIbanWithLock("IBAN9999999999999999999999999999")).thenReturn(Optional.of(receiver));

        Transfer result = transferService.performTransfer(transfer);

        assertThat(result.getIbanFrom()).isEqualTo("IBAN1111111111111111111111111111");
        assertThat(result.getIbanTo()).isEqualTo("IBAN9999999999999999999999999999");
        assertThat(result.getSentAmount()).isEqualByComparingTo("100.00");
        assertThat(result.getSenderBalance()).isEqualByComparingTo("900.00");
        assertThat(result.getReceiverBalance()).isEqualByComparingTo("600.00");

        verify(accountRepository).findByIbanWithLock("IBAN1111111111111111111111111111");
        verify(accountRepository).findByIbanWithLock("IBAN9999999999999999999999999999");
        verify(currencyRateService, never()).getTodayRate(any());
    }

    @Test
    void performTransfer_WhenBothInBYN_ShouldTransferCorrectly() {
        sender.setCurrency(AccountCurrency.BYN);
        receiver.setCurrency(AccountCurrency.BYN);
        when(accountRepository.findByIbanWithLock("IBAN1111111111111111111111111111")).thenReturn(Optional.of(sender));
        when(accountRepository.findByIbanWithLock("IBAN9999999999999999999999999999")).thenReturn(Optional.of(receiver));

        Transfer result = transferService.performTransfer(transfer);

        assertThat(result.getSenderBalance()).isEqualByComparingTo("900.00");
        assertThat(result.getReceiverBalance()).isEqualByComparingTo("600.00");
        verify(currencyRateService, never()).getTodayRate(any());
    }

    @Test
    void performTransfer_WhenSenderNotBYN_ShouldConvertToReceiverCurrency() {
        when(accountRepository.findByIbanWithLock("IBAN1111111111111111111111111111")).thenReturn(Optional.of(sender));
        when(accountRepository.findByIbanWithLock("IBAN9999999999999999999999999999")).thenReturn(Optional.of(receiver));
        when(currencyRateService.getTodayRate(AccountCurrency.USD)).thenReturn(usdRate);
        when(currencyRateService.getTodayRate(AccountCurrency.EUR)).thenReturn(eurRate);

        Transfer result = transferService.performTransfer(transfer);

        // USD 100 * 3.03 = 303 BYN
        // 303 BYN / 3.5 = 86.57 EUR
        assertThat(result.getSenderBalance()).isEqualByComparingTo("900.00");
        assertThat(result.getReceiverBalance()).isEqualByComparingTo("586.57"); // 500 + 86.57

        verify(currencyRateService).getTodayRate(AccountCurrency.USD);
        verify(currencyRateService).getTodayRate(AccountCurrency.EUR);
    }

    @Test
    void performTransfer_WhenSenderBYNReceiverNotBYN_ShouldConvertCorrectly() {
        sender.setCurrency(AccountCurrency.BYN);
        when(accountRepository.findByIbanWithLock("IBAN1111111111111111111111111111")).thenReturn(Optional.of(sender));
        when(accountRepository.findByIbanWithLock("IBAN9999999999999999999999999999")).thenReturn(Optional.of(receiver));
        when(currencyRateService.getTodayRate(AccountCurrency.EUR)).thenReturn(eurRate);

        Transfer result = transferService.performTransfer(transfer);

        // 100 BYN / 3.5 = 28.57 EUR
        assertThat(result.getSenderBalance()).isEqualByComparingTo("900.00");
        assertThat(result.getReceiverBalance()).isEqualByComparingTo("528.57"); // 500 + 28.57

        verify(currencyRateService, never()).getTodayRate(AccountCurrency.USD);
        verify(currencyRateService).getTodayRate(AccountCurrency.EUR);
    }

    @Test
    void performTransfer_WhenSenderNotBYNReceiverBYN_ShouldConvertCorrectly() {
        receiver.setCurrency(AccountCurrency.BYN);
        when(accountRepository.findByIbanWithLock("IBAN1111111111111111111111111111")).thenReturn(Optional.of(sender));
        when(accountRepository.findByIbanWithLock("IBAN9999999999999999999999999999")).thenReturn(Optional.of(receiver));
        when(currencyRateService.getTodayRate(AccountCurrency.USD)).thenReturn(usdRate);

        Transfer result = transferService.performTransfer(transfer);

        // USD 100 * 3.03 = 303 BYN
        assertThat(result.getSenderBalance()).isEqualByComparingTo("900.00");
        assertThat(result.getReceiverBalance()).isEqualByComparingTo("803.00"); // 500 + 303

        verify(currencyRateService).getTodayRate(AccountCurrency.USD);
        verify(currencyRateService, never()).getTodayRate(AccountCurrency.EUR);
    }

    @Test
    void performTransfer_WhenSenderNotFound_ShouldThrowEntityNotFoundException() {
        when(accountRepository.findByIbanWithLock("IBAN1111111111111111111111111111")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transferService.performTransfer(transfer))
                .isInstanceOf(EntityNotFoundException.class);

        verify(accountRepository).findByIbanWithLock("IBAN1111111111111111111111111111");
        verify(accountRepository, never()).findByIbanWithLock("IBAN9999999999999999999999999999");
        verify(currencyRateService, never()).getTodayRate(any());
    }

    @Test
    void performTransfer_WhenReceiverNotFound_ShouldThrowEntityNotFoundException() {
        when(accountRepository.findByIbanWithLock("IBAN1111111111111111111111111111")).thenReturn(Optional.of(sender));
        when(accountRepository.findByIbanWithLock("IBAN9999999999999999999999999999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transferService.performTransfer(transfer))
                .isInstanceOf(EntityNotFoundException.class);

        verify(accountRepository).findByIbanWithLock("IBAN1111111111111111111111111111");
        verify(accountRepository).findByIbanWithLock("IBAN9999999999999999999999999999");
        verify(currencyRateService, never()).getTodayRate(any());
    }

    @Test
    void performTransfer_WhenSenderNotActive_ShouldThrowIllegalArgumentException() {
        sender.setStatus(AccountStatus.BLOCKED);
        when(accountRepository.findByIbanWithLock("IBAN1111111111111111111111111111")).thenReturn(Optional.of(sender));
        when(accountRepository.findByIbanWithLock("IBAN9999999999999999999999999999")).thenReturn(Optional.of(receiver));

        assertThatThrownBy(() -> transferService.performTransfer(transfer))
                .isInstanceOf(IllegalArgumentException.class);

        verify(currencyRateService, never()).getTodayRate(any());
    }

    @Test
    void performTransfer_WhenReceiverNotActive_ShouldThrowIllegalArgumentException() {
        receiver.setStatus(AccountStatus.BLOCKED);
        when(accountRepository.findByIbanWithLock("IBAN1111111111111111111111111111")).thenReturn(Optional.of(sender));
        when(accountRepository.findByIbanWithLock("IBAN9999999999999999999999999999")).thenReturn(Optional.of(receiver));

        assertThatThrownBy(() -> transferService.performTransfer(transfer))
                .isInstanceOf(IllegalArgumentException.class);

        verify(currencyRateService, never()).getTodayRate(any());
    }

    @Test
    void performTransfer_WhenSameIban_ShouldThrowIllegalArgumentException() {
        transfer.setIbanTo("IBAN1111111111111111111111111111");
        when(accountRepository.findByIbanWithLock("IBAN1111111111111111111111111111")).thenReturn(Optional.of(sender));
        when(accountRepository.findByIbanWithLock("IBAN1111111111111111111111111111")).thenReturn(Optional.of(sender));

        assertThatThrownBy(() -> transferService.performTransfer(transfer))
                .isInstanceOf(IllegalArgumentException.class);

        verify(currencyRateService, never()).getTodayRate(any());
    }

    @Test
    void performTransfer_WhenInsufficientFunds_ShouldThrowIllegalArgumentException() {
        transfer.setSentAmount(new BigDecimal("1500.00"));
        when(accountRepository.findByIbanWithLock("IBAN1111111111111111111111111111")).thenReturn(Optional.of(sender));
        when(accountRepository.findByIbanWithLock("IBAN9999999999999999999999999999")).thenReturn(Optional.of(receiver));

        assertThatThrownBy(() -> transferService.performTransfer(transfer))
                .isInstanceOf(IllegalArgumentException.class);

        verify(currencyRateService, never()).getTodayRate(any());
    }

    @Test
    void performTransfer_ShouldUpdateAccountBalancesInRepository() {
        when(accountRepository.findByIbanWithLock("IBAN1111111111111111111111111111")).thenReturn(Optional.of(sender));
        when(accountRepository.findByIbanWithLock("IBAN9999999999999999999999999999")).thenReturn(Optional.of(receiver));
        when(currencyRateService.getTodayRate(AccountCurrency.USD)).thenReturn(usdRate);
        when(currencyRateService.getTodayRate(AccountCurrency.EUR)).thenReturn(eurRate);

        transferService.performTransfer(transfer);

        assertThat(sender.getBalance()).isEqualByComparingTo("900.00");
        assertThat(receiver.getBalance()).isEqualByComparingTo("586.57");
    }

    @Test
    void performTransfer_WhenSendAmountHas2Decimals_ShouldRoundCorrectly() {
        transfer.setSentAmount(new BigDecimal("100.55"));
        when(accountRepository.findByIbanWithLock("IBAN1111111111111111111111111111")).thenReturn(Optional.of(sender));
        when(accountRepository.findByIbanWithLock("IBAN9999999999999999999999999999")).thenReturn(Optional.of(receiver));
        when(currencyRateService.getTodayRate(AccountCurrency.USD)).thenReturn(usdRate);
        when(currencyRateService.getTodayRate(AccountCurrency.EUR)).thenReturn(eurRate);

        Transfer result = transferService.performTransfer(transfer);

        // 100.55 * 3.03 = 304.6665
        // 304.6665 / 3.5 = 87.0475...
        assertThat(result.getSenderBalance()).isEqualByComparingTo("899.45");
        assertThat(result.getReceiverBalance()).isEqualByComparingTo("587.05");
    }

    @Test
    void performTransfer_WithLargeScaleCurrency_ShouldHandleCorrectly() {
        CurrencyRate rubRate = new CurrencyRate();
        rubRate.setCurrency(AccountCurrency.RUB);
        rubRate.setRate(new BigDecimal("3.0"));
        rubRate.setScale(100);

        sender.setCurrency(AccountCurrency.RUB);
        sender.setBalance(new BigDecimal("100000"));
        transfer.setSentAmount(new BigDecimal("10000"));

        receiver.setCurrency(AccountCurrency.BYN);

        when(accountRepository.findByIbanWithLock("IBAN1111111111111111111111111111")).thenReturn(Optional.of(sender));
        when(accountRepository.findByIbanWithLock("IBAN9999999999999999999999999999")).thenReturn(Optional.of(receiver));
        when(currencyRateService.getTodayRate(AccountCurrency.RUB)).thenReturn(rubRate);

        Transfer result = transferService.performTransfer(transfer);

        // 10000 * 3.0 / 100 = 300 BYN
        assertThat(result.getSenderBalance()).isEqualByComparingTo("90000");
        assertThat(result.getReceiverBalance()).isEqualByComparingTo("800.00");
    }

    @Test
    void performTransfer_ShouldRoundToTwoDecimalPlaces() {
        CurrencyRate preciseRate = new CurrencyRate();
        preciseRate.setCurrency(AccountCurrency.USD);
        preciseRate.setRate(new BigDecimal("3.33333"));
        preciseRate.setScale(1);

        when(accountRepository.findByIbanWithLock("IBAN1111111111111111111111111111")).thenReturn(Optional.of(sender));
        when(accountRepository.findByIbanWithLock("IBAN9999999999999999999999999999")).thenReturn(Optional.of(receiver));
        when(currencyRateService.getTodayRate(AccountCurrency.USD)).thenReturn(preciseRate);
        when(currencyRateService.getTodayRate(AccountCurrency.EUR)).thenReturn(eurRate);

        Transfer result = transferService.performTransfer(transfer);

        assertThat(result.getReceiverBalance().scale()).isEqualTo(2);
    }
}