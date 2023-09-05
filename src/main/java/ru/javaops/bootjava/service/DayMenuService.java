package ru.javaops.bootjava.service;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.bootjava.error.DataConflictException;
import ru.javaops.bootjava.error.IllegalRequestDataException;
import ru.javaops.bootjava.error.NotFoundException;
import ru.javaops.bootjava.repository.DayMenuRepository;
import ru.javaops.bootjava.repository.MealRepository;
import ru.javaops.bootjava.repository.RestaurantRepository;
import ru.javaops.bootjava.repository.VoteRepository;
import ru.javaops.bootjava.repository.model.*;
import ru.javaops.bootjava.to.*;
import ru.javaops.bootjava.web.AuthUser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class DayMenuService {

    private final Logger log = getLogger(getClass());
    private final DayMenuRepository dayMenuRepository;

    private final RestaurantRepository restaurantRepository;
    private final MealRepository mealRepository;
    private final VoteRepository voteRepository;

    public DayMenuService(DayMenuRepository dayMenuRepository, RestaurantRepository restaurantRepository,
                          MealRepository mealRepository, VoteRepository voteRepository) {
        this.dayMenuRepository = dayMenuRepository;
        this.restaurantRepository = restaurantRepository;
        this.mealRepository = mealRepository;
        this.voteRepository = voteRepository;
    }
    
    @Transactional(readOnly = true)
    public DayMenuResponse get(UUID id) {
        log.info("get {}", id);
        DayMenu dayMenu = dayMenuRepository.findByIdOrThrow(id);
        return toResponse(dayMenu);
    }

    @Transactional(readOnly = true)
    public List<DayMenuResponse> getAll(LocalDate date) {
        log.info("getAll");
        List<DayMenu> dayMenus;
        if (date == null)
            dayMenus = dayMenuRepository.getAllWithMeals();
        else dayMenus = dayMenuRepository.getAllWithMealsByDate(date);
        return dayMenus.stream().map(this::toResponse).toList();
    }

    @Transactional
    public void delete(UUID id) {
        dayMenuRepository.deleteExisted(id);
    }

    @Transactional
    public DayMenuResponse create(DayMenuCreateOrUpdateRequest request) {
        log.info("create {}", request);
        Restaurant restaurant = restaurantRepository.findByIdOrThrow(request.restaurantId());
        if (dayMenuRepository.existsByMenuDateAndRestaurant(request.date(), restaurant)) {
            throw new DataConflictException(String.format("Restaurant %s already has menu on date %s",
                    restaurant.getName(), request.date()));
        }
        List<Meal> meals = mealRepository.findAllById(request.mealIds());

        validateMeals(meals, request.mealIds());

        DayMenu created = dayMenuRepository.save(toEntity(request.date(), meals, restaurant));
        return toResponse(created);
    }

    @Transactional
    public DayMenuResponse update(DayMenuCreateOrUpdateRequest request, UUID id) {
        log.info("update {} with id={}", request, id);

        DayMenu dayMenu = dayMenuRepository.findByIdOrThrow(id);
        Restaurant restaurant = restaurantRepository.findByIdOrThrow(request.restaurantId());
        List<Meal> meals = mealRepository.findAllById(request.mealIds());

        validateMeals(meals, request.mealIds());
        DayMenu updated = dayMenuRepository.save(updateEntity(dayMenu, request.date(), meals, restaurant));
        return toResponse(updated);
    }

    @Transactional
    public DayMenuResponse vote(UUID dayMenuId, AuthUser authUser) {
        log.info("User {} voted for day menu {}", authUser.id(), dayMenuId);
        User user = authUser.getUser();
        Vote vote = voteRepository.findByCreatedAndUser(LocalDate.now(), user).orElse(null);
        if (vote != null) {
            if (LocalTime.now().isAfter(LocalTime.of(11, 0))) {
                throw new IllegalRequestDataException("It is not possible to revote after 11 A.M.");
            } else {
                dayMenuRepository.decrementVotesCounter(vote.getDayMenu().id());
                voteRepository.delete(vote);
            }
        }
        int updatedRows = dayMenuRepository.incrementVotesCounter(dayMenuId);
        if (updatedRows == 0) throw new NotFoundException("Day menu was not found by id " + dayMenuId);
        DayMenu dayMenu = dayMenuRepository.findByIdOrThrow(dayMenuId);

        Vote newVote = new Vote(
                dayMenu,
                user
        );
        dayMenu.addVote(newVote);
        return toResponse(dayMenu);
    }

    private void validateMeals(List<Meal> meals, Set<UUID> mealIds) {
        Set<UUID> foundIds = meals.stream().map(Meal::id).collect(Collectors.toSet());
        mealIds.removeAll(foundIds);
        if (!mealIds.isEmpty()) {
            throw new NotFoundException(String.format("Meal(s) with id(s) %s not found",
                    mealIds.stream().map(UUID::toString).collect(Collectors.joining(", "))));
        }

    }

    private DayMenu toEntity(LocalDate date, List<Meal> meals, Restaurant restaurant) {
        return new DayMenu(
                date,
                Set.copyOf(meals),
                restaurant
        );
    }

    private DayMenu updateEntity(DayMenu dayMenu, LocalDate date, List<Meal> meals, Restaurant restaurant) {
        dayMenu.setMenuDate(date);
        dayMenu.setMeals(Set.copyOf(meals));
        dayMenu.setRestaurant(restaurant);
        return dayMenu;
    }

    private DayMenuResponse toResponse(DayMenu dm) {
        return new DayMenuResponse(
                dm.id(),
                dm.getMenuDate(),
                dm.getMeals().stream().map(m -> new MealResponse(m.id(), m.getName(), m.getPrice(), null)).collect(Collectors.toSet()),
                dm.getRestaurant().toResponse(),
                dm.getVotesCounter());
    }


}
