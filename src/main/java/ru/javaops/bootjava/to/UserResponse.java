package ru.javaops.bootjava.to;

import ru.javaops.bootjava.repository.model.Role;

import java.util.Set;
import java.util.UUID;

public record UserResponse(

        UUID id,

        String name,

        String email,

        Set<Role> roles
) {

}
