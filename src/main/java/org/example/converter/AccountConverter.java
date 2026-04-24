package org.example.converter;

import org.example.dto.CreateAccountRequestDto;
import org.example.dto.AccountResponseDto;
import org.example.entity.AccountEntity;
import org.example.model.Account;
import org.springframework.stereotype.Component;

import java.util.Currency;
import java.util.List;

@Component
public class AccountConverter {

    public AccountEntity toEntity(Account model) {
        AccountEntity entity = new AccountEntity();
        entity.setId(model.getId());
        entity.setBalance(model.getBalance());
        entity.setCurrency(model.getCurrency().toString());
        entity.setStatus(model.getStatus());
        return entity;
    }

    public Account toModel(AccountEntity entity) {
        Account model = new Account();
        model.setId(entity.getId());
        model.setBalance(entity.getBalance());
        try {
            model.setCurrency(Currency.getInstance(entity.getCurrency()));
        } catch(IllegalArgumentException e) {
            throw new IllegalArgumentException("%s isn't valid ISO 4217 code".formatted(entity.getCurrency()));
        }
        model.setStatus(entity.getStatus());
        return model;
    }

    public Account toModel(CreateAccountRequestDto dto) {
        Account model = new Account();
        model.setBalance(dto.getBalance());
        try {
            model.setCurrency(Currency.getInstance(dto.getCurrency()));
        } catch(IllegalArgumentException e) {
            throw new IllegalArgumentException("%s isn't valid ISO 4217 code".formatted(dto.getCurrency()));
        }
        return model;
    }

    public List<Account> toModels(List<AccountEntity> entities) {
        return entities.stream()
                .map(this::toModel)
                .toList();
    }

    public AccountResponseDto toDto(Account model) {
        AccountResponseDto dto = new AccountResponseDto();
        dto.setId(model.getId());
        dto.setBalance(model.getBalance());
        dto.setCurrency(model.getCurrency().toString());
        dto.setStatus(model.getStatus());
        return dto;
    }

    public List<AccountResponseDto> toDtos(List<Account> models) {
        return models.stream()
                .map(this::toDto)
                .toList();
    }
}
