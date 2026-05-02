package org.example.converter;

import java.util.List;

public interface ModelToEntityConverter <M, E> {

    E toEntity(M model);

    default List<E> toEntities(List<M> models) {
        return models.stream()
                .map(this::toEntity)
                .toList();
    }
}