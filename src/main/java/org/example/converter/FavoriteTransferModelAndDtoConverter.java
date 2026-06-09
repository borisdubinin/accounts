package org.example.converter;

import org.example.dto.CreateFavoriteTransferRequestDto;
import org.example.dto.FavoriteTransferResponseDto;
import org.example.model.FavoriteTransfer;
import org.springframework.stereotype.Component;

@Component
public class FavoriteTransferModelAndDtoConverter implements
        ModelToDtoConverter<FavoriteTransfer, FavoriteTransferResponseDto>,
        DtoToModelConverter<CreateFavoriteTransferRequestDto, FavoriteTransfer> {

    @Override
    public FavoriteTransfer toModel(CreateFavoriteTransferRequestDto dto) {
        return FavoriteTransfer.builder()
                .ibanFrom(dto.getIbanFrom())
                .ibanTo(dto.getIbanTo())
                .amount(dto.getAmount())
                .build();
    }

    @Override
    public FavoriteTransferResponseDto toDto(FavoriteTransfer model) {
        return FavoriteTransferResponseDto.builder()
                .id(model.getId())
                .ibanFrom(model.getIbanFrom())
                .ibanTo(model.getIbanTo())
                .amount(model.getAmount())
                .build();
    }
}
