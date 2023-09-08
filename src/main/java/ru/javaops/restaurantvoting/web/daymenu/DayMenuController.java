package ru.javaops.restaurantvoting.web.daymenu;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javaops.restaurantvoting.service.DayMenuService;
import ru.javaops.restaurantvoting.to.DayMenuCreateOrUpdateRequest;
import ru.javaops.restaurantvoting.to.DayMenuResponse;
import ru.javaops.restaurantvoting.util.WebUtil;


import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static ru.javaops.restaurantvoting.web.daymenu.DayMenuController.REST_URL;

@Tag(name = "Menu of the day")
@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class DayMenuController {
    static final String REST_URL = "/admin/api/v1/day-menu";

    private final DayMenuService service;

    public DayMenuController(DayMenuService service) {
        this.service = service;
    }

    @Operation(summary = "Get day menu by id")
    @GetMapping("/{id}")
    public DayMenuResponse get(@PathVariable UUID id) {
        return service.get(id);
    }

    @Operation(summary = "Delete day menu id")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @Operation(summary = "Get list of day menus")
    @GetMapping()
    public List<DayMenuResponse> getAll(@RequestParam(required = false) LocalDate date) {
        return service.getAll(date);
    }


    @Operation(summary = "Create day menu")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DayMenuResponse> createWithLocation(@Valid @RequestBody DayMenuCreateOrUpdateRequest request) {
        DayMenuResponse response = service.create(request);
        URI uri = WebUtil.getEntityUri(response.id());
        return ResponseEntity.created(uri).body(response);
    }

    @Operation(summary = "Update day menu by id")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public DayMenuResponse update(@PathVariable UUID id,
                                  @Valid @RequestBody DayMenuCreateOrUpdateRequest request) {
        return service.update(request, id);
    }

}
