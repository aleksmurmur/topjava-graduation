package ru.javaops.restaurantvoting.to;

import java.util.UUID;

public record RestaurantResponse(
        UUID id,
        String name
) {
}
