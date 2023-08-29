package ru.javaops.bootjava.to;

import java.util.UUID;

public record RestaurantResponse(
        UUID id,
        String name
) {
}
