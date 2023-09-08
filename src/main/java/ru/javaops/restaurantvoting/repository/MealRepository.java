package ru.javaops.restaurantvoting.repository;

import ru.javaops.restaurantvoting.repository.model.Meal;
import ru.javaops.restaurantvoting.repository.model.Restaurant;

import java.util.List;

public interface MealRepository extends BaseRepository<Meal> {

    List<Meal> findAllByRestaurant(Restaurant restaurant);
}
