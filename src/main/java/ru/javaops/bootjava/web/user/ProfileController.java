package ru.javaops.bootjava.web.user;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.javaops.bootjava.service.ProfileService;
import ru.javaops.bootjava.to.UserCreateOrUpdateRequest;
import ru.javaops.bootjava.to.UserResponse;
import ru.javaops.bootjava.util.WebUtil;
import ru.javaops.bootjava.web.AuthUser;

import java.net.URI;



@RestController
@RequestMapping(value = ProfileController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileController {
    static final String REST_URL = "/api/profile";

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

    @GetMapping
    public ResponseEntity<UserResponse> get(@AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(service.getCurrent(authUser));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        service.delete(authUser.id());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserCreateOrUpdateRequest request) {
        UserResponse response = service.register(request);
        URI uriOfNewResource = WebUtil.getEntityUri(response.id());
        return ResponseEntity.created(uriOfNewResource).body(response);
    }


    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<UserResponse> update(@RequestBody @Valid UserCreateOrUpdateRequest request, @AuthenticationPrincipal AuthUser authUser) {
        UserResponse response = service.update(request, authUser);
        return ResponseEntity.ok(response);
    }
}