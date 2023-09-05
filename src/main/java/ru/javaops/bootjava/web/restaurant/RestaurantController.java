package ru.javaops.bootjava.web.restaurant;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javaops.bootjava.service.RestaurantService;
import ru.javaops.bootjava.to.RestaurantCreateOrUpdateRequest;
import ru.javaops.bootjava.to.RestaurantResponse;
import ru.javaops.bootjava.util.WebUtil;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Tag(name = "Рестораны")
@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {

    static final String REST_URL = "/admin/api/v1/restaurants";

    private final RestaurantService service;

    public RestaurantController(RestaurantService service) {
        this.service = service;
    }


    @Operation(summary = "Получить ресторан по id")
    @GetMapping("/{id}")
    public RestaurantResponse get(@PathVariable UUID id) {
        return service.get(id);
    }

    @Operation(summary = "Удалить ресторан по id")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @Operation(summary = "Получить список ресторанов")
    @GetMapping
    public List<RestaurantResponse> getAll() {
        return service.getAll();
    }

    @Operation(summary = "Создать ресторан")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantResponse> createWithLocation(@Valid @RequestBody RestaurantCreateOrUpdateRequest request) {
        RestaurantResponse response = service.create(request);
        URI uri = WebUtil.getEntityUri(response.id());
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public RestaurantResponse update(@Valid @RequestBody RestaurantCreateOrUpdateRequest request,
                                                     @PathVariable UUID id) {
        return service.update(request, id);
    }

}
