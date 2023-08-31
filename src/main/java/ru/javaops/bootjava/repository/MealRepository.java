package ru.javaops.bootjava.repository;

import ru.javaops.bootjava.repository.model.Meal;
import ru.javaops.bootjava.repository.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface MealRepository extends BaseRepository<Meal> {

    List<Meal> findAllByRestaurant(Restaurant restaurant);
}
