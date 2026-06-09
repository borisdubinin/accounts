package org.example.converter;

import org.example.entity.TransferEntity;
import org.example.model.Transfer;
import org.springframework.stereotype.Component;

@Component
public class TransferEntityAndModelConverter implements
        EntityToModelConverter<TransferEntity, Transfer>,
        ModelToEntityConverter<Transfer, TransferEntity> {

    @Override
    public Transfer toModel(TransferEntity entity) {
        return Transfer.builder()
                .ibanFrom(entity.getIbanFrom())
                .ibanTo(entity.getIbanTo())
                .sentAmount(entity.getAmount())
                .senderCurrency(entity.getSenderCurrency())
                .receiverCurrency(entity.getReceiverCurrency())
                .senderBalance(entity.getSenderBalance())
                .receiverBalance(entity.getReceiverBalance())
                .dateTime(entity.getCreatedAt())
                .build();
    }

    @Override
    public TransferEntity toEntity(Transfer model) {
        TransferEntity entity = new TransferEntity();
        entity.setIbanFrom(model.getIbanFrom());
        entity.setIbanTo(model.getIbanTo());
        entity.setAmount(model.getSentAmount());
        entity.setSenderCurrency(model.getSenderCurrency());
        entity.setReceiverCurrency(model.getReceiverCurrency());
        entity.setSenderBalance(model.getSenderBalance());
        entity.setReceiverBalance(model.getReceiverBalance());
        return entity;
    }
}
