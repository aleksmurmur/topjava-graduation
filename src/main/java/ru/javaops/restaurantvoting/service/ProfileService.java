package ru.javaops.restaurantvoting.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.repository.UserRepository;
import ru.javaops.restaurantvoting.repository.model.Role;
import ru.javaops.restaurantvoting.repository.model.User;
import ru.javaops.restaurantvoting.to.UserCreateOrUpdateRequest;
import ru.javaops.restaurantvoting.to.UserResponse;
import ru.javaops.restaurantvoting.web.AuthUser;

import java.util.Set;

@Service
@Slf4j
public class ProfileService extends AbstractUserService {
    public ProfileService(UserRepository repository) {
        super(repository);
    }

    @Transactional(readOnly = true)
    public UserResponse getCurrent(AuthUser authUser) {
        log.info("get {}", authUser);
        return toResponse(authUser.getUser());
    }

    @Transactional
    public UserResponse register(UserCreateOrUpdateRequest request) {
        log.info("register {}", request);
        User created = repository.save(request.toEntity(Set.of(Role.USER)));
        return toResponse(created);
    }

    @Transactional
    public UserResponse update(UserCreateOrUpdateRequest request, AuthUser authUser) {
        log.info("update {} with id {}", request, authUser.id());
        User user = authUser.getUser();
        User updated = repository.save(update(user, request, user.getRoles()));
        return toResponse(updated);

    }
}
