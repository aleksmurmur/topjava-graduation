package ru.javaops.bootjava.to;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.javaops.bootjava.repository.model.Role;
import ru.javaops.bootjava.repository.model.User;
import ru.javaops.bootjava.util.validation.NoHtml;

import java.util.Set;

import static ru.javaops.bootjava.config.SecurityConfig.PASSWORD_ENCODER;

public record UserCreateOrUpdateRequest(
        @NotBlank
        @Size(min = 2, max = 128)
        @NoHtml
        String name,

        @Email
        @NotBlank
        @Size(max = 128)
        @NoHtml
        String email,

        @NotBlank
        @Size(min = 5, max = 32)
        String password,

        Set<Role> roles

) {
        public UserCreateOrUpdateRequest(String name, String email, String password) {
                this(name, email, password, Set.of(Role.USER));
        }

        public User toEntity(Set<Role> roles) {
                return new User(name(), email().toLowerCase(), PASSWORD_ENCODER.encode(password()), roles.isEmpty() ? Set.of(Role.USER) : roles);
        }
}
