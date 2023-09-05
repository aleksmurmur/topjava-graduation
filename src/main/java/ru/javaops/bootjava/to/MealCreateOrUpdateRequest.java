package ru.javaops.bootjava.to;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;
import ru.javaops.bootjava.util.validation.NoHtml;

import java.util.UUID;

public record MealCreateOrUpdateRequest(
        @NotBlank
        @Size(min = 2, max = 120)
        @NoHtml
        String name,
        @NotNull
        @Range(min = 0)
        long price,
        @NotNull
        UUID restaurantId
) {
}
