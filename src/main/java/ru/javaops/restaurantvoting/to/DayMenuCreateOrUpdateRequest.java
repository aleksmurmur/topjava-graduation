package ru.javaops.restaurantvoting.to;

import jakarta.validation.constraints.NotNull;
import ru.javaops.restaurantvoting.util.validation.NotEmptyCollection;
import ru.javaops.restaurantvoting.util.validation.NotPast;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record DayMenuCreateOrUpdateRequest(
        @NotNull
        @NotPast
        LocalDate date,
        @NotNull
        @NotEmptyCollection
        Set<UUID> mealIds,
        @NotNull
        UUID restaurantId
) {
}
