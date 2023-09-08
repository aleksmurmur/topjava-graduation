package ru.javaops.restaurantvoting.service;

import org.slf4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurantvoting.repository.RestaurantRepository;
import ru.javaops.restaurantvoting.repository.model.Restaurant;
import ru.javaops.restaurantvoting.to.RestaurantCreateOrUpdateRequest;
import ru.javaops.restaurantvoting.to.RestaurantResponse;

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

    @Transactional(readOnly = true)
    public RestaurantResponse get(UUID id) {
        log.info("get {}", id);
        Restaurant restaurant = repository.findByIdOrThrow(id);
        return toResponse(restaurant);
    }

    @Cacheable("restaurant")
    @Transactional(readOnly = true)
    public List<RestaurantResponse> getAll() {
        log.info("getAll");
        List<Restaurant> restaurants = repository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        return restaurants.stream().map(this::toResponse).toList();
    }

    @CacheEvict(value = "restaurant", allEntries = true)
    @Transactional
    public void delete(UUID id) {
        repository.deleteExisted(id);
    }

    @CacheEvict(value = "restaurant", allEntries = true)
    @Transactional
    public RestaurantResponse create(RestaurantCreateOrUpdateRequest request) {
        log.info("create {}", request);

        Restaurant created = repository.save(toEntity(request));
        return toResponse(created);
    }

    @CacheEvict(value = "restaurant", allEntries = true)
    @Transactional
    public RestaurantResponse update(RestaurantCreateOrUpdateRequest request, UUID id) {
        log.info("update {} with id={}", request, id);

        Restaurant restaurant = repository.findByIdOrThrow(id);
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
