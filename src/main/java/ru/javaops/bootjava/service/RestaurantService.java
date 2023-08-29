package ru.javaops.bootjava.service;

import org.slf4j.Logger;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.javaops.bootjava.repository.RestaurantRepository;
import ru.javaops.bootjava.repository.model.Restaurant;
import ru.javaops.bootjava.to.RestaurantCreateOrUpdateRequest;
import ru.javaops.bootjava.to.RestaurantResponse;

import java.util.List;
import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class RestaurantService {


    private final Logger log = getLogger(getClass());

    private final RestaurantRepository repository;

    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    public RestaurantResponse get(@PathVariable UUID id) {
        Restaurant restaurant = repository.getExisted(id);
        return toResponse(restaurant);
    }

    public List<RestaurantResponse> getAll() {
        log.info("getAll");
        List<Restaurant> restaurants = repository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        return restaurants.stream().map(this::toResponse).toList();
    }

    public void delete(UUID id) {
        repository.deleteExisted(id);
    }

    public RestaurantResponse create(RestaurantCreateOrUpdateRequest request) {
        log.info("create {}", request);

        Restaurant created = repository.save(toEntity(request));
        return toResponse(created);
    }

    public RestaurantResponse update(RestaurantCreateOrUpdateRequest request, UUID id) {
        log.info("update {} with id={}", request, id);

        Restaurant restaurant = repository.getExisted(id);
        Restaurant updated = repository.save(updateEntity(restaurant, request));
        return toResponse(updated);
    }

    private Restaurant toEntity(RestaurantCreateOrUpdateRequest request) {
        return new Restaurant(request.name());
    }

    private Restaurant updateEntity(Restaurant restaurant, RestaurantCreateOrUpdateRequest request) {
        restaurant.setName(request.name());
        return restaurant;
    }

    private RestaurantResponse toResponse(Restaurant r) {
        return new RestaurantResponse(r.getId(), r.getName());
    }
}