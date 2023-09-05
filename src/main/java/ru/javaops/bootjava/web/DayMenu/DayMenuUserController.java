package ru.javaops.bootjava.web.DayMenu;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.javaops.bootjava.service.DayMenuService;
import ru.javaops.bootjava.to.DayMenuResponse;
import ru.javaops.bootjava.web.AuthUser;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static ru.javaops.bootjava.web.DayMenu.DayMenuUserController.REST_URL;


@Tag(name = "Меню дня")
@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class DayMenuUserController {
    static final String REST_URL = "/user/api/v1/day-menu";

    private final DayMenuService service;

    public DayMenuUserController(DayMenuService service) {
        this.service = service;
    }

    @Operation(summary = "Получить меню ресторанов на текущую дату")
    @GetMapping()
    public List<DayMenuResponse> getAll() {
        return service.getAll(LocalDate.now());
    }

    @Operation(summary = "Проголосовать за меню")
    @PutMapping("/{dayMenuId}")
    public DayMenuResponse vote(@PathVariable UUID dayMenuId, @AuthenticationPrincipal AuthUser authUser) {
        return service.vote(dayMenuId, authUser);
    }
}
