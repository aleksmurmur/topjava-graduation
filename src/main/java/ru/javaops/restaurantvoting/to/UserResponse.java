package ru.javaops.restaurantvoting.to;

import ru.javaops.restaurantvoting.repository.model.Role;

import java.util.Set;
import java.util.UUID;

public record UserResponse(

        UUID id,

        String name,

        String email,

        Set<Role> roles
) {

}
