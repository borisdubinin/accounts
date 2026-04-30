package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.converter.AccountConverter;
import org.example.dto.*;
import org.example.model.Account;
import org.example.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Accounts management", description = "API for accounts management")
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AccountConverter accountConverter;

    @Operation(
            summary = "Create new account",
            description = "Creates an account with the specified details",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Account was successfully created",
                            content = @Content(schema = @Schema(implementation = AccountResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request")
            })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponseDto create(@Valid @RequestBody CreateAccountRequestDto createAccountRequestDto) {
        Account account = accountConverter.toModel(createAccountRequestDto);
        Account newAccount = accountService.create(account);
        return accountConverter.toDto(newAccount);
    }

    @Operation(
            summary = "Get all accounts",
            description = "Returns list of all accounts",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Accounts retrieved successfully", content =
                    @Content(array = @ArraySchema(schema = @Schema(implementation = AccountResponseDto.class)))                    )
            })
    @GetMapping
    public List<AccountResponseDto> getAll() {
        List<Account> allAccounts = accountService.getAll();
        return accountConverter.toDtos(allAccounts);
    }

    @Operation(
            summary = "Get balance",
            description = "Returns balance and currency of the account",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Balance was successfully retrieved",
                            content = @Content(schema = @Schema(implementation = GetBalanceResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "404", description =
                            "Specified resource didn't found (example: non-existent IBAN of the account)")
            })
    @GetMapping("/balance")
    public GetBalanceResponseDto getBalance(@Valid @RequestBody GetBalanceRequestDto getBalanceRequestDto) {
        String iban = getBalanceRequestDto.getIban();
        Account account = accountService.getByIban(iban);
        return new GetBalanceResponseDto(account.getBalance(), account.getCurrency());
    }
}