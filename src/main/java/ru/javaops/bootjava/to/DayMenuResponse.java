package ru.javaops.bootjava.to;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

public record DayMenuResponse(
        UUID id,
        LocalDate date,
        Set<MealResponse> meals,
        RestaurantResponse restaurant,
        int votes
) {
}
