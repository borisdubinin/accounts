package org.example.converter;

import org.example.dto.CreateAccountRequestDto;
import org.example.dto.AccountResponseDto;
import org.example.entity.AccountEntity;
import org.example.model.Account;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountConverter {

    public AccountEntity toEntity(Account model) {
        AccountEntity entity = new AccountEntity();
        entity.setId(model.getId());
        entity.setBalance(model.getBalance());
        entity.setCurrency(model.getCurrency());
        entity.setStatus(model.getStatus());
        entity.setIban(model.getIban());
        entity.setSettings(model.getSettings());
        return entity;
    }

    public Account toModel(AccountEntity entity) {
        Account model = new Account();
        model.setId(entity.getId());
        model.setBalance(entity.getBalance());
        model.setCurrency(entity.getCurrency());
        model.setStatus(entity.getStatus());
        model.setIban(entity.getIban());
        model.setSettings(entity.getSettings());
        return model;
    }

    public Account toModel(CreateAccountRequestDto dto) {
        Account model = new Account();
        model.setBalance(dto.getBalance());
        model.setCurrency(dto.getCurrency());
        model.setSettings(dto.getSettings());
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
        dto.setCurrency(model.getCurrency());
        dto.setStatus(model.getStatus());
        dto.setIban(model.getIban());
        dto.setSettings(model.getSettings());
        return dto;
    }

    public List<AccountResponseDto> toDtos(List<Account> models) {
        return models.stream()
                .map(this::toDto)
                .toList();
    }
}
