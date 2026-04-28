package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.Account;
import org.example.model.AccountCurrency;
import org.example.model.CurrencyRate;
import org.example.model.Transfer;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final AccountService accountService;
    private final CurrencyRateService currencyRateService;

    private static final int DECIMAL_SCALE = 2;

    @Override
    @Transactional
    public Transfer performTransfer(Transfer transfer) {
        Account senderAccount = accountService.getByIban(transfer.getIbanFrom());
        Account receiverAccount = accountService.getByIban(transfer.getIbanTo());

        validateBeforeTransfer(transfer.getSentAmount(), senderAccount.getBalance());

        BigDecimal receivedAmount = convertAmountToReceiverCurrency(
                transfer.getSentAmount(), senderAccount.getCurrency(), receiverAccount.getCurrency());

        BigDecimal senderFinalBalance = senderAccount.getBalance().subtract(transfer.getSentAmount());
        BigDecimal receiverFinalBalance = receiverAccount.getBalance().add(receivedAmount);

        senderAccount.setBalance(senderFinalBalance);
        receiverAccount.setBalance(receiverFinalBalance);

        Account updatedSenderAccount = accountService.update(senderAccount);
        Account updatedRecipientAccount = accountService.update(receiverAccount);

        return Transfer.builder()
                .ibanFrom(updatedSenderAccount.getIban())
                .senderBalance(updatedSenderAccount.getBalance())
                .senderCurrency(updatedSenderAccount.getCurrency())
                .sentAmount(transfer.getSentAmount())
                .ibanTo(updatedRecipientAccount.getIban())
                .receiverBalance(updatedRecipientAccount.getBalance())
                .receiverCurrency(updatedRecipientAccount.getCurrency())
                .build();
    }

    private void validateBeforeTransfer(BigDecimal sentAmount, BigDecimal senderBalance) {
        if (sentAmount.compareTo(senderBalance) > 0) {
            throw new IllegalArgumentException(
                    "Impossible to perform transfer: current balance(%s) is less than the transfer amount(%s)"
                            .formatted(senderBalance, sentAmount)
            );
        }
    }

    private @NonNull BigDecimal convertAmountToReceiverCurrency(
            BigDecimal sentAmount,
            AccountCurrency senderAccountCurrency,
            AccountCurrency receiverAccountCurrency
    ) {
        BigDecimal bynSentAmount;
        if (senderAccountCurrency != AccountCurrency.BYN) {
            CurrencyRate senderCurrencyRate = currencyRateService.getTodayRate(senderAccountCurrency);
            bynSentAmount = sentAmount
                    .multiply(senderCurrencyRate.getRate())
                    .divide(new BigDecimal(senderCurrencyRate.getScale()), DECIMAL_SCALE, RoundingMode.HALF_UP);
        } else {
            bynSentAmount = sentAmount;
        }

        if (receiverAccountCurrency != AccountCurrency.BYN) {
            CurrencyRate receiverCurrencyRate = currencyRateService.getTodayRate(receiverAccountCurrency);
            return bynSentAmount
                    .multiply(new BigDecimal(receiverCurrencyRate.getScale()))
                    .divide(receiverCurrencyRate.getRate(), DECIMAL_SCALE, RoundingMode.HALF_UP);
        } else {
            return bynSentAmount;
        }
    }
}