package ru.javaops.bootjava.web.meal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javaops.bootjava.service.MealService;
import ru.javaops.bootjava.to.MealCreateOrUpdateRequest;
import ru.javaops.bootjava.to.MealResponse;
import ru.javaops.bootjava.util.WebUtil;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Tag(name = "Еда")
@RestController
@RequestMapping(value = MealController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MealController {
    static final String REST_URL = "/admin/api/v1/meals";

    private final MealService service;

    public MealController(MealService service) {
        this.service = service;
    }


    @Operation(summary = "Получить блюдо по id")
    @GetMapping("/{id}")
    public MealResponse get(@PathVariable UUID id) {
        return service.get(id);
    }

    @Operation(summary = "Удалить блюдо по id")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @Operation(summary = "Получить список блюд")
    @GetMapping()
    public List<MealResponse> getAll(@RequestParam(required = false) UUID restaurantId) {
        return service.getAll(restaurantId);
    }


    @Operation(summary = "Создать блюдо")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MealResponse> createWithLocation(@Valid @RequestBody MealCreateOrUpdateRequest request) {
        MealResponse response = service.create(request);
        URI uri = WebUtil.getEntityUri(response.id());
        return ResponseEntity.created(uri).body(response);
    }

    @Operation(summary = "Обновить блюдо")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public MealResponse update(@Valid @RequestBody MealCreateOrUpdateRequest request,
                                                     @PathVariable UUID id) {
        return service.update(request, id);
    }


}
