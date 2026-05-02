package org.example.converter;

import java.util.List;

public interface DtoToModelConverter <D, M> {

    M toModel(D dto);

    default List<M> toModels(List<D> dtos) {
        return dtos.stream()
                .map(this::toModel)
                .toList();
    }
}