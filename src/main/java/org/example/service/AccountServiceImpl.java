package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.converter.AccountConverter;
import org.example.entity.AccountEntity;
import org.example.model.AccountStatus;
import org.example.exception.EntityNotFoundException;
import org.example.model.Account;
import org.example.repository.AccountRepository;
import org.example.util.IbanGenerator;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountConverter accountConverter;

    @Override
    public Account create(Account account) {
        account.setStatus(AccountStatus.ACTIVE);
        account.setIban(IbanGenerator.generateIban());
        AccountEntity entity = accountConverter.toEntity(account);
        AccountEntity newEntity = accountRepository.save(entity);
        return accountConverter.toModel(newEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Account> getAll() {
        List<AccountEntity> accountEntities = accountRepository.findAll();
        return accountConverter.toModels(accountEntities);
    }

    @Cacheable(value = "balance")
    @Transactional(readOnly = true)
    @Override
    public Account getByIban(String iban) {
        AccountEntity accountEntity = accountRepository.findByIban(iban)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Account not found with IBAN: %s".formatted(iban)));
        return accountConverter.toModel(accountEntity);
    }
}
