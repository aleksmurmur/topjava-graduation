package ru.javaops.bootjava.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.javaops.bootjava.repository.UserRepository;
import ru.javaops.bootjava.repository.model.Role;
import ru.javaops.bootjava.repository.model.User;
import ru.javaops.bootjava.to.UserCreateOrUpdateRequest;
import ru.javaops.bootjava.to.UserResponse;
import ru.javaops.bootjava.web.AuthUser;
import ru.javaops.bootjava.web.user.UniqueMailValidator;

import java.util.Set;

@Service
@Slf4j
public class ProfileService extends AbstractUserService {
    public ProfileService(UserRepository repository) {
        super(repository);
    }

    public UserResponse getCurrent(AuthUser authUser) {
        log.info("get {}", authUser);
        return toResponse(authUser.getUser());
    }

    public UserResponse register(UserCreateOrUpdateRequest request) {
        log.info("register {}", request);
        User created = repository.save(request.toEntity(Set.of(Role.USER)));
        return toResponse(created);
    }

    public UserResponse update(UserCreateOrUpdateRequest request, AuthUser authUser) {
        log.info("update {} with id {}", request, authUser.id());
        User user = authUser.getUser();
        User updated = repository.save(update(user, request, user.getRoles()));
        return toResponse(updated);

    }
}
