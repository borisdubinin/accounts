package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.converter.AccountConverter;
import org.example.entity.AccountEntity;
import org.example.entity.AccountStatus;
import org.example.model.Account;
import org.example.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountConverter accountConverter;

    @Transactional
    public Account create(Account account) {
        account.setStatus(AccountStatus.ACTIVE);
        AccountEntity entity = accountConverter.toEntity(account);
        AccountEntity newEntity = accountRepository.save(entity);
        return accountConverter.toModel(newEntity);
    }

    @Transactional(readOnly = true)
    public List<Account> getAll() {
        List<AccountEntity> accountEntities = accountRepository.findAll();
        return accountConverter.toModels(accountEntities);
    }
}
