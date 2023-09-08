package ru.javaops.restaurantvoting.web.daymenu;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.javaops.restaurantvoting.service.DayMenuService;
import ru.javaops.restaurantvoting.to.DayMenuResponse;
import ru.javaops.restaurantvoting.web.AuthUser;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static ru.javaops.restaurantvoting.web.daymenu.DayMenuUserController.REST_URL;


@Tag(name = "Day menu")
@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class DayMenuUserController {
    static final String REST_URL = "/user/api/v1/day-menu";

    private final DayMenuService service;

    public DayMenuUserController(DayMenuService service) {
        this.service = service;
    }

    @Operation(summary = "Get restaurants day menu list for today")
    @GetMapping()
    public List<DayMenuResponse> getAll() {
        return service.getAll(LocalDate.now());
    }

    @Operation(summary = "Vote for menu", description = "It is possible to revote if current time is less then 11 A.M.")
    @PutMapping("/{dayMenuId}")
    public DayMenuResponse vote(@PathVariable UUID dayMenuId, @AuthenticationPrincipal AuthUser authUser) {
        return service.vote(dayMenuId, authUser);
    }
}
