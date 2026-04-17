package org.example.converter;

import org.example.entity.AccountEntity;
import org.example.model.Account;
import org.springframework.stereotype.Component;

import java.util.Currency;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class AccountConverter {

    public AccountEntity toEntity(Account model) {
        AccountEntity entity = new AccountEntity();
        entity.setId(model.getId());
        entity.setBalance(model.getBalance());
        entity.setCurrency(Objects.toString(model.getCurrency(), null));
        entity.setStatus(model.getStatus());
        return entity;
    }

    public Account toModel(AccountEntity entity) {
        Account model = new Account();
        model.setId(entity.getId());
        model.setBalance(entity.getBalance());
        model.setCurrency(Optional.ofNullable(entity.getCurrency()).map(Currency::getInstance).orElse(null));
        model.setStatus(entity.getStatus());
        return model;
    }

    public List<Account> toModels(List<AccountEntity> entities) {
        return entities.stream()
                .map(this::toModel)
                .toList();
    }
}
