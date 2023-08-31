package ru.javaops.bootjava.to;

import ru.javaops.bootjava.repository.model.NamedEntity;

import java.util.UUID;

public record MealResponse(
        UUID id,
        String name,
        long price,
        NamedElement<UUID> restaurant
) {
}
