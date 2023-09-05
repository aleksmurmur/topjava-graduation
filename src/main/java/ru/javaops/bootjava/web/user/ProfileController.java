package ru.javaops.bootjava.web.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.javaops.bootjava.service.ProfileService;
import ru.javaops.bootjava.to.UserCreateOrUpdateRequest;
import ru.javaops.bootjava.to.UserResponse;
import ru.javaops.bootjava.util.WebUtil;
import ru.javaops.bootjava.web.AuthUser;

import java.net.URI;


@Tag(name = "Профиль пользователя")
@RestController
@RequestMapping(value = ProfileController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileController {
    static final String REST_URL = "/user/api/v1/profile";

    private final ProfileService service;
    private final UniqueMailValidator emailValidator;
    public ProfileController(ProfileService service, UniqueMailValidator emailValidator) {
        this.service = service;
        this.emailValidator = emailValidator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }

    @Operation(description = "Получить текущего пользователя")
    @GetMapping
    public UserResponse get(@AuthenticationPrincipal AuthUser authUser) {
        return service.getCurrent(authUser);
    }

    @Operation(description = "Удалить текущего пользователя")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        service.delete(authUser.id());
    }

    @Operation(description = "Зарегистрироваться")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserCreateOrUpdateRequest request) {
        UserResponse response = service.register(request);
        URI uriOfNewResource = WebUtil.getEntityUri(response.id());
        return ResponseEntity.created(uriOfNewResource).body(response);
    }

    @Operation(description = "Обновить информацию в профиле")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse update(@RequestBody @Valid UserCreateOrUpdateRequest request, @AuthenticationPrincipal AuthUser authUser) {
        return service.update(request, authUser);
    }
}