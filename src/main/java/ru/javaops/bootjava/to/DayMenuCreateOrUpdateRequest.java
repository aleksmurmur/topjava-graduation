package ru.javaops.bootjava.to;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record DayMenuCreateOrUpdateRequest(
        LocalDate date,
        Set<UUID> mealIds,
        UUID restaurantId
) {
}
