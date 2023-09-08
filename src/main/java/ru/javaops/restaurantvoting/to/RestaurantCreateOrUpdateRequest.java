package ru.javaops.restaurantvoting.to;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.javaops.restaurantvoting.util.validation.NoHtml;

public record RestaurantCreateOrUpdateRequest(
        @NotBlank
        @Size(min = 2, max = 128)
        @NoHtml
        String name
) {
}
