package ru.javaops.restaurantvoting.service;

import org.slf4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import ru.javaops.restaurantvoting.repository.MealRepository;
import ru.javaops.restaurantvoting.repository.RestaurantRepository;
import ru.javaops.restaurantvoting.repository.model.Meal;
import ru.javaops.restaurantvoting.repository.model.Restaurant;
import ru.javaops.restaurantvoting.to.MealCreateOrUpdateRequest;
import ru.javaops.restaurantvoting.to.MealResponse;
import ru.javaops.restaurantvoting.to.NamedElement;

import java.util.List;
import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class MealService {

    private final Logger log = getLogger(getClass());

    private final MealRepository repository;
    private final RestaurantRepository restaurantRepository;

    public MealService(MealRepository repository, RestaurantRepository restaurantRepository) {
        this.repository = repository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional(readOnly = true)
    public MealResponse get(@PathVariable UUID id) {
        log.info("get {}", id);
        Meal meal = repository.findByIdOrThrow(id);
        return toResponse(meal);
    }

    @Cacheable("meal")
    @Transactional(readOnly = true)
    public List<MealResponse> getAll(UUID restaurantId) {
        log.info("getAll");
        List<Meal> meals;
        if (restaurantId == null)
            meals = repository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        else {
            Restaurant restaurant = restaurantRepository.findByIdOrThrow(restaurantId);
            meals = repository.findAllByRestaurant(restaurant);
        }

        return meals.stream().map(this::toResponse).toList();
    }

    @CacheEvict(value = "meal", allEntries = true)
    @Transactional
    public void delete(UUID id) {
        repository.deleteExisted(id);
    }

    @CacheEvict(value = "meal", allEntries = true)
    @Transactional
    public MealResponse create(MealCreateOrUpdateRequest request) {
        log.info("create {}", request);
        Meal created = repository.save(toEntity(request));
        return toResponse(created);
    }

    @CacheEvict(value = "meal", allEntries = true)
    @Transactional
    public MealResponse update(MealCreateOrUpdateRequest request, UUID id) {
        log.info("update {} with id={}", request, id);

        Meal meal = repository.findByIdOrThrow(id);
        Meal updated = repository.save(updateEntity(meal, request));
        return toResponse(updated);
    }

    private Meal toEntity(MealCreateOrUpdateRequest request) {
        Restaurant restaurant = restaurantRepository.findByIdOrThrow(request.restaurantId());
        return new Meal(request.name(), request.price(), restaurant);
    }

    private Meal updateEntity(Meal meal, MealCreateOrUpdateRequest request) {
        Restaurant restaurant = restaurantRepository.findByIdOrThrow(request.restaurantId());
        meal.setName(request.name());
        meal.setPrice(request.price());
        meal.setRestaurant(restaurant);
        return meal;
    }

    private MealResponse toResponse(Meal m) {
        Restaurant r = m.getRestaurant();
        return new MealResponse(
                m.getId(),
                m.getName(),
                m.getPrice(),
                new NamedElement<>(r.getId(), r.getName()));
    }
}
