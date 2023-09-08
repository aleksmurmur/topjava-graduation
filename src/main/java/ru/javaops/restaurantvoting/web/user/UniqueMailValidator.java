package ru.javaops.restaurantvoting.web.user;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import ru.javaops.restaurantvoting.repository.UserRepository;
import ru.javaops.restaurantvoting.to.UserCreateOrUpdateRequest;
import ru.javaops.restaurantvoting.web.AuthUser;

import java.util.UUID;

@Component
@AllArgsConstructor
public class UniqueMailValidator implements org.springframework.validation.Validator {
    public static final String EXCEPTION_DUPLICATE_EMAIL = "User with this email already exists";

    private final UserRepository repository;
    private final HttpServletRequest request;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return UserCreateOrUpdateRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        UserCreateOrUpdateRequest user = ((UserCreateOrUpdateRequest) target);
        if (StringUtils.hasText(user.email())) {
            repository.findByEmailIgnoreCase(user.email())
                    .ifPresent(dbUser -> {
                        if (request.getMethod().equals("PUT")) {  // UPDATE
                            UUID dbId = dbUser.id();

                            String requestURI = request.getRequestURI();
                            if (requestURI.endsWith("/" + dbId) || (dbId == AuthUser.authId() && requestURI.contains("/profile")))
                                return;
                        }
                        errors.rejectValue("email", "", EXCEPTION_DUPLICATE_EMAIL);
                    });
        }
    }
}
