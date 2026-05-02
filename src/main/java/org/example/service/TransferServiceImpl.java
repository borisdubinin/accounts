package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.annotation.LogExecutionTime;
import org.example.entity.AccountEntity;
import org.example.exception.EntityNotFoundException;
import org.example.model.AccountCurrency;
import org.example.model.AccountStatus;
import org.example.model.CurrencyRate;
import org.example.model.Transfer;
import org.example.repository.AccountRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private static final int AMOUNT_SCALE = 2;

    private final AccountRepository accountRepository;
    private final CurrencyRateService currencyRateService;

    @Override
    @Transactional
    @LogExecutionTime
    public Transfer performTransfer(Transfer transfer) {
        AccountEntity sender = accountRepository.findByIbanWithLock(transfer.getIbanFrom())
                .orElseThrow(() -> new EntityNotFoundException("Account not found with IBAN: %s".formatted(transfer.getIbanFrom())));
        AccountEntity receiver = accountRepository.findByIbanWithLock(transfer.getIbanTo())
                .orElseThrow(() -> new EntityNotFoundException("Account not found with IBAN: %s".formatted(transfer.getIbanTo())));

        validateBeforeTransfer(sender, receiver, transfer);

        BigDecimal receivedAmount = convertAmountToReceiverCurrency(
                transfer.getSentAmount(), sender.getCurrency(), receiver.getCurrency());

        BigDecimal senderFinalBalance = sender.getBalance().subtract(transfer.getSentAmount());
        BigDecimal receiverFinalBalance = receiver.getBalance().add(receivedAmount);

        sender.setBalance(senderFinalBalance);
        receiver.setBalance(receiverFinalBalance);

        return Transfer.builder()
                .ibanFrom(sender.getIban())
                .senderBalance(sender.getBalance())
                .senderCurrency(sender.getCurrency())
                .sentAmount(transfer.getSentAmount())
                .ibanTo(receiver.getIban())
                .receiverBalance(receiver.getBalance())
                .receiverCurrency(receiver.getCurrency())
                .build();
    }

    private void validateBeforeTransfer(AccountEntity sender, AccountEntity receiver, Transfer transfer) {
        if (sender.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalArgumentException("Impossible to perform transfer: sender account isn't active");
        } else if (receiver.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalArgumentException("Impossible to perform transfer: receiver account isn't active");
        }
        if (Objects.equals(sender.getIban(), receiver.getIban())) {
            throw new IllegalArgumentException(
                    "Impossible to perform transfer: receiver IBAN(%s) is equals to sender IBAN".formatted(transfer.getIbanFrom()));
        } else if (transfer.getSentAmount().compareTo(sender.getBalance()) > 0) {
            throw new IllegalArgumentException(
                    "Impossible to perform transfer: current balance(%s) is less than the transfer amount(%s)".formatted(sender.getBalance(), transfer.getSentAmount()));
        }
    }

    private @NonNull BigDecimal convertAmountToReceiverCurrency(
            BigDecimal sentAmount,
            AccountCurrency senderAccountCurrency,
            AccountCurrency receiverAccountCurrency) {
        if(senderAccountCurrency == receiverAccountCurrency) {
            return sentAmount;
        }

        BigDecimal bynSentAmount;
        if (senderAccountCurrency != AccountCurrency.BYN) {
            bynSentAmount = convertSendAmountToByn(sentAmount, senderAccountCurrency);
        } else {
            bynSentAmount = sentAmount;
        }

        if (receiverAccountCurrency != AccountCurrency.BYN) {
            return convertSendAmountToReceiverCurrency(receiverAccountCurrency, bynSentAmount);
        } else {
            return bynSentAmount;
        }
    }

    private @NonNull BigDecimal convertSendAmountToByn(BigDecimal sentAmount, AccountCurrency senderAccountCurrency) {
        CurrencyRate senderCurrencyRate = currencyRateService.getTodayRate(senderAccountCurrency);
        return sentAmount
                .multiply(senderCurrencyRate.getRate())
                .divide(new BigDecimal(senderCurrencyRate.getScale()), AMOUNT_SCALE, RoundingMode.HALF_UP);
    }

    private @NonNull BigDecimal convertSendAmountToReceiverCurrency(AccountCurrency receiverAccountCurrency, BigDecimal bynSentAmount) {
        CurrencyRate receiverCurrencyRate = currencyRateService.getTodayRate(receiverAccountCurrency);
        return bynSentAmount
                .multiply(new BigDecimal(receiverCurrencyRate.getScale()))
                .divide(receiverCurrencyRate.getRate(), AMOUNT_SCALE, RoundingMode.HALF_UP);
    }
}