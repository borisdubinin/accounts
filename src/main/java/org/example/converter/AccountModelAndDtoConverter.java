package org.example.converter;

import org.example.dto.AccountResponseDto;
import org.example.dto.AccountSettingsDto;
import org.example.dto.CreateAccountRequestDto;
import org.example.model.Account;
import org.example.model.AccountSettings;
import org.springframework.stereotype.Component;

@Component
public class AccountModelAndDtoConverter implements
        ModelToDtoConverter<Account, AccountResponseDto>,
        DtoToModelConverter<CreateAccountRequestDto, Account> {

    @Override
    public Account toModel(CreateAccountRequestDto dto) {
        Account model = new Account();
        model.setBalance(dto.getBalance());
        model.setCurrency(dto.getCurrency());
        model.setSettings(dto.getSettings() != null
                ? new AccountSettings(
                dto.getSettings().getMonthlyLimit(),
                dto.getSettings().getSmsNotificationsEnabled())
                : null);
        return model;
    }

    @Override
    public AccountResponseDto toDto(Account model) {
        AccountResponseDto dto = new AccountResponseDto();
        dto.setId(model.getId());
        dto.setBalance(model.getBalance());
        dto.setCurrency(model.getCurrency());
        dto.setStatus(model.getStatus());
        dto.setIban(model.getIban());
        dto.setSettings(new AccountSettingsDto(
                model.getSettings().getMonthlyLimit(),
                model.getSettings().getSmsNotificationsEnabled()));
        return dto;
    }
}