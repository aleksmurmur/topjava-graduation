package ru.javaops.bootjava.web.user;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.javaops.bootjava.service.AdminUserService;
import ru.javaops.bootjava.to.UserCreateOrUpdateRequest;
import ru.javaops.bootjava.to.UserResponse;
import ru.javaops.bootjava.util.WebUtil;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = AdminUserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminUserController {
    static final String REST_URL = "/admin/api/v1/users";

    private final AdminUserService service;
    private final UniqueMailValidator emailValidator;
    public AdminUserController(AdminUserService service, UniqueMailValidator emailValidator) {
        this.service = service;
        this.emailValidator = emailValidator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable UUID id) {
        return service.get(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @GetMapping
    public List<UserResponse> getAll() {
        return service.findAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> createWithLocation(@Valid @RequestBody UserCreateOrUpdateRequest request) {
        UserResponse response = service.create(request);
        URI uri = WebUtil.getEntityUri(response.id());
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse update(@Valid @RequestBody UserCreateOrUpdateRequest request, @PathVariable UUID id) {
        return service.update(request, id);
    }

    @GetMapping("/by-email")
    public UserResponse getByEmail(@RequestParam String email) {
        return service.getByEmail(email);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable UUID id, @RequestParam boolean enabled) {
        service.enable(id, enabled);
    }
}