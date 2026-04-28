package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.example.model.AccountCurrency;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferResponseDto {

    @Schema(description = "IBAN of the account from which money was send", example = "IBANQEDESSDGPEB3FR9H8QZ2MOT1S58A")
    private String ibanFrom;

    @Schema(description = "Balance of the account from which money was send", example = "1234.00")
    private BigDecimal senderBalance;

    @Schema(description = "Account currency of the account from which money was send")
    private AccountCurrency senderCurrency;

    @Schema(description = "Amount of money which was send", example = "1234.00")
    private BigDecimal amount;

    @Schema(description = "IBAN of the account to which money was send", example = "IBANQEDESSDGPEB3FR9H8QZ2MOT1S58A")
    private String ibanTo;

    @Schema(description = "Balance of the account to which money was send", example = "1234.00")
    private BigDecimal receiverBalance;

    @Schema(description = "Account currency of the account to which money was send")
    private AccountCurrency receiverCurrency;
}