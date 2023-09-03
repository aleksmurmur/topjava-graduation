package ru.javaops.bootjava.service;

import org.slf4j.Logger;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import ru.javaops.bootjava.repository.UserRepository;
import ru.javaops.bootjava.repository.model.Role;
import ru.javaops.bootjava.repository.model.User;
import ru.javaops.bootjava.to.UserCreateOrUpdateRequest;
import ru.javaops.bootjava.to.UserResponse;
import ru.javaops.bootjava.web.user.UniqueMailValidator;

import java.util.Set;
import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javaops.bootjava.config.SecurityConfig.PASSWORD_ENCODER;


public abstract class AbstractUserService {

    private final Logger log = getLogger(getClass());

    protected UserRepository repository;

    public AbstractUserService(UserRepository repository) {
        this.repository = repository;
    }

    public UserResponse get(UUID id) {
        log.info("get {}", id);
        User user = repository.findByIdOrThrow(id);
        return toResponse(user);
    }

    public void delete(UUID id) {
        log.info("delete {}", id);
        repository.deleteExisted(id);
    }

    protected UserResponse toResponse(User user) {
        return new UserResponse(user.id(), user.getName(), user.getEmail(), user.getRoles());
    }

    protected User update(User user, UserCreateOrUpdateRequest request, Set<Role> roles) {
        user.setName(request.name());
        user.setEmail(request.email().toLowerCase());
        user.setPassword(PASSWORD_ENCODER.encode(request.password()));
        user.setRoles(roles);
        return user;
    }

}
