package org.example.converter;

import java.util.List;

public interface EntityToModelConverter<E, M> {

    M toModel(E entity);

    default List<M> toModels(List<E> entities) {
        return entities.stream()
                .map(this::toModel)
                .toList();
    }
}