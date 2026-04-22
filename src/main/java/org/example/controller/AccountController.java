package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.converter.AccountConverter;
import org.example.dto.AccountRequestDto;
import org.example.dto.AccountResponseDto;
import org.example.model.Account;
import org.example.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AccountConverter accountConverter;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponseDto create(@Valid @RequestBody AccountRequestDto accountRequestDto) {
        Account account = accountConverter.toModel(accountRequestDto);
        Account newAccount = accountService.create(account);
        return accountConverter.toDto(newAccount);
    }

    @GetMapping
    public List<AccountResponseDto> getAll() {
        List<Account> allAccounts = accountService.getAll();
        return accountConverter.toDtos(allAccounts);
    }
}
