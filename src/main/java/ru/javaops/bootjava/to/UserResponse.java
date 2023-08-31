package ru.javaops.bootjava.to;

import java.util.UUID;

public record UserResponse(

        UUID id,

        String name,

        String email
) {

}
