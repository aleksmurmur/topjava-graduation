package ru.javaops.bootjava.to;

import java.util.UUID;

public record MealCreateOrUpdateRequest(
        String name,
        long price,
        UUID restaurantId
) {
}
