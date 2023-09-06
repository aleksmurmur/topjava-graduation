package ru.javaops.bootjava.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.bootjava.error.NotFoundException;
import ru.javaops.bootjava.repository.UserRepository;
import ru.javaops.bootjava.repository.model.User;
import ru.javaops.bootjava.to.UserCreateOrUpdateRequest;
import ru.javaops.bootjava.to.UserResponse;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AdminUserService extends AbstractUserService {
    public AdminUserService(UserRepository repository) {
        super(repository);
    }

    @Cacheable("users")
    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        log.info("getAll");
        List<User> users = repository.findAll(Sort.by(Sort.Direction.ASC, "name", "email"));
        return users.stream().map(this::toResponse).toList();
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public UserResponse create(UserCreateOrUpdateRequest request) {
        log.info("create {}", request);
        User created = repository.save(request.toEntity(request.roles()));
        return toResponse(created);
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public UserResponse update(UserCreateOrUpdateRequest request, UUID id) {
        log.info("update {} with id={}", request, id);
        User user = repository.findById(id).orElseThrow(() -> new NotFoundException("User with id=" + id + " not found"));
        User updated = repository.save(update(user, request, request.roles()));
        return toResponse(updated);
    }

    @Transactional(readOnly = true)
    public UserResponse getByEmail(String email) {
        log.info("getByEmail {}", email);
        User user = repository.getByEmail(email);
        return toResponse(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void enable(UUID id, boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        User user = repository.findByIdOrThrow(id);
        user.setEnabled(enabled);
    }
}
