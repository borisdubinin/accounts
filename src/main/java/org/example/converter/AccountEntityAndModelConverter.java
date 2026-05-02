package org.example.converter;

import org.example.entity.AccountEntity;
import org.example.entity.AccountSettingsEntity;
import org.example.model.Account;
import org.example.model.AccountSettings;
import org.springframework.stereotype.Component;

@Component
public class AccountEntityAndModelConverter implements
        EntityToModelConverter<AccountEntity, Account>,
        ModelToEntityConverter<Account, AccountEntity> {

    @Override
    public Account toModel(AccountEntity entity) {
        Account model = new Account();
        model.setId(entity.getId());
        model.setBalance(entity.getBalance());
        model.setCurrency(entity.getCurrency());
        model.setStatus(entity.getStatus());
        model.setIban(entity.getIban());
        model.setSettings(new AccountSettings(
                entity.getSettings().getMonthlyLimit(),
                entity.getSettings().getSmsNotificationsEnabled()));
        return model;
    }

    @Override
    public AccountEntity toEntity(Account model) {
        AccountEntity entity = new AccountEntity();
        entity.setId(model.getId());
        entity.setBalance(model.getBalance());
        entity.setCurrency(model.getCurrency());
        entity.setStatus(model.getStatus());
        entity.setIban(model.getIban());
        entity.setSettings(new AccountSettingsEntity(
                model.getSettings().getMonthlyLimit(),
                model.getSettings().getSmsNotificationsEnabled()
        ));
        return entity;
    }
}