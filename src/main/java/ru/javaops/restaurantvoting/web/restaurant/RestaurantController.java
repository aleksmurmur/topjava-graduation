package ru.javaops.restaurantvoting.web.restaurant;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javaops.restaurantvoting.service.RestaurantService;
import ru.javaops.restaurantvoting.to.RestaurantCreateOrUpdateRequest;
import ru.javaops.restaurantvoting.to.RestaurantResponse;
import ru.javaops.restaurantvoting.util.WebUtil;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Tag(name = "Restaurants")
@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {

    static final String REST_URL = "/admin/api/v1/restaurants";

    private final RestaurantService service;

    public RestaurantController(RestaurantService service) {
        this.service = service;
    }


    @Operation(summary = "Get restaurant by id")
    @GetMapping("/{id}")
    public RestaurantResponse get(@PathVariable UUID id) {
        return service.get(id);
    }

    @Operation(summary = "Delete restaurant by id")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @Operation(summary = "Get restaurants list")
    @GetMapping
    public List<RestaurantResponse> getAll() {
        return service.getAll();
    }

    @Operation(summary = "Create restaurant")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantResponse> createWithLocation(@Valid @RequestBody RestaurantCreateOrUpdateRequest request) {
        RestaurantResponse response = service.create(request);
        URI uri = WebUtil.getEntityUri(response.id());
        return ResponseEntity.created(uri).body(response);
    }

    @Operation(summary = "Update restaurant by ud")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public RestaurantResponse update(@PathVariable UUID id,
                                     @Valid @RequestBody RestaurantCreateOrUpdateRequest request) {
        return service.update(request, id);
    }

}
