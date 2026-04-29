package org.example.converter;

import org.example.dto.GetBalanceResponseDto;
import org.example.model.Account;
import org.springframework.stereotype.Component;

@Component
public class BalanceConverter {

    public GetBalanceResponseDto toDto(Account model) {
        var dto = new GetBalanceResponseDto();
        dto.setBalance(model.getBalance());
        dto.setCurrency(model.getCurrency());
        return dto;
    }
}
