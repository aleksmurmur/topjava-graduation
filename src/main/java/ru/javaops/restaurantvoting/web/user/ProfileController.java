package ru.javaops.restaurantvoting.web.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.javaops.restaurantvoting.service.ProfileService;
import ru.javaops.restaurantvoting.to.UserCreateOrUpdateRequest;
import ru.javaops.restaurantvoting.to.UserResponse;
import ru.javaops.restaurantvoting.util.WebUtil;
import ru.javaops.restaurantvoting.web.AuthUser;

import java.net.URI;


@Tag(name = "Profile")
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

    @Operation(summary = "Get profile info")
    @GetMapping
    public UserResponse get(@AuthenticationPrincipal AuthUser authUser) {
        return service.getCurrent(authUser);
    }

    @Operation(summary = "Delete profile")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        service.delete(authUser.id());
    }

    @Operation(summary = "Register")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserCreateOrUpdateRequest request) {
        UserResponse response = service.register(request);
        URI uriOfNewResource = WebUtil.getEntityUri(response.id());
        return ResponseEntity.created(uriOfNewResource).body(response);
    }

    @Operation(summary = "Update profile info")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse update(@RequestBody @Valid UserCreateOrUpdateRequest request, @AuthenticationPrincipal AuthUser authUser) {
        return service.update(request, authUser);
    }
}