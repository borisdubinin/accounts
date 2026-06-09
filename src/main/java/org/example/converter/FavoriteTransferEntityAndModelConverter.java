package org.example.converter;

import org.example.entity.FavoriteTransferEntity;
import org.example.model.FavoriteTransfer;
import org.springframework.stereotype.Component;

@Component
public class FavoriteTransferEntityAndModelConverter implements
        EntityToModelConverter<FavoriteTransferEntity, FavoriteTransfer>,
        ModelToEntityConverter<FavoriteTransfer, FavoriteTransferEntity> {

    @Override
    public FavoriteTransfer toModel(FavoriteTransferEntity entity) {
        return FavoriteTransfer.builder()
                .id(entity.getId())
                .ibanFrom(entity.getIbanFrom())
                .ibanTo(entity.getIbanTo())
                .amount(entity.getAmount())
                .build();
    }

    @Override
    public FavoriteTransferEntity toEntity(FavoriteTransfer model) {
        return FavoriteTransferEntity.builder()
                .id(model.getId())
                .ibanFrom(model.getIbanFrom())
                .ibanTo(model.getIbanTo())
                .amount(model.getAmount())
                .build();
    }
}
