package org.example.converter;

import java.util.List;

public interface ModelToDtoConverter <M, D> {

    D toDto(M model);

    default List<D> toDtos(List<M> models) {
        return models.stream()
                .map(this::toDto)
                .toList();
    }
}