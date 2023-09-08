package ru.javaops.restaurantvoting.to;

import java.util.UUID;

public record MealResponse(
        UUID id,
        String name,
        long price,
        NamedElement<UUID> restaurant
) {
}
