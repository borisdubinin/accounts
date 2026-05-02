package org.example.converter;

import org.example.dto.TransferRequestDto;
import org.example.dto.TransferResponseDto;
import org.example.model.Transfer;
import org.springframework.stereotype.Component;

@Component
public class TransferModelAndDtoConverter implements
        ModelToDtoConverter<Transfer, TransferResponseDto>,
        DtoToModelConverter<TransferRequestDto, Transfer> {

    @Override
    public Transfer toModel(TransferRequestDto dto) {
        return Transfer.builder()
                .ibanFrom(dto.getIbanFrom())
                .ibanTo(dto.getIbanTo())
                .sentAmount(dto.getSentAmount())
                .build();
    }

    @Override
    public TransferResponseDto toDto(Transfer transfer) {
        TransferResponseDto dto = new TransferResponseDto();
        dto.setIbanFrom(transfer.getIbanFrom());
        dto.setSenderBalance(transfer.getSenderBalance());
        dto.setSenderCurrency(transfer.getSenderCurrency());
        dto.setAmount(transfer.getSentAmount());
        dto.setIbanTo(transfer.getIbanTo());
        dto.setReceiverBalance(transfer.getReceiverBalance());
        dto.setReceiverCurrency(transfer.getReceiverCurrency());
        return dto;
    }
}