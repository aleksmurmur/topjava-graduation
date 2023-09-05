package ru.javaops.bootjava.web.daymenu;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javaops.bootjava.service.DayMenuService;
import ru.javaops.bootjava.to.DayMenuCreateOrUpdateRequest;
import ru.javaops.bootjava.to.DayMenuResponse;
import ru.javaops.bootjava.util.WebUtil;


import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static ru.javaops.bootjava.web.daymenu.DayMenuController.REST_URL;

@Tag(name = "Меню дня")
@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class DayMenuController {
    static final String REST_URL = "/admin/api/v1/day-menu";

    private final DayMenuService service;

    public DayMenuController(DayMenuService service) {
        this.service = service;
    }

    @Operation(summary = "Получить меню по id")
    @GetMapping("/{id}")
    public DayMenuResponse get(@PathVariable UUID id) {
        return service.get(id);
    }

    @Operation(summary = "Удалить меню по id")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @Operation(summary = "Получить список меню")
    @GetMapping()
    public List<DayMenuResponse> getAll(@RequestParam LocalDate date) {
        return service.getAll(date);
    }


    @Operation(summary = "Создать меню дня")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DayMenuResponse> createWithLocation(@Valid @RequestBody DayMenuCreateOrUpdateRequest request) {
        DayMenuResponse response = service.create(request);
        URI uri = WebUtil.getEntityUri(response.id());
        return ResponseEntity.created(uri).body(response);
    }

    @Operation(summary = "Обновить меню")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public DayMenuResponse update(@PathVariable UUID id,
                                  @Valid @RequestBody DayMenuCreateOrUpdateRequest request) {
        return service.update(request, id);
    }

}
