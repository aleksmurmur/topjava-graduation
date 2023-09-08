package ru.javaops.restaurantvoting.to;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record DayMenuResponse(
        UUID id,
        LocalDate menuDate,
        Set<MealResponse> meals,
        RestaurantResponse restaurant,
        int votes
) {
}
