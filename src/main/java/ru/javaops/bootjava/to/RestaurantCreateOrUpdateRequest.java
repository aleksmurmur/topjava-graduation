package ru.javaops.bootjava.to;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.javaops.bootjava.util.validation.NoHtml;

public record RestaurantCreateOrUpdateRequest(
        @NotBlank
        @Size(min = 2, max = 128)
        @NoHtml
        String name
) {
}
