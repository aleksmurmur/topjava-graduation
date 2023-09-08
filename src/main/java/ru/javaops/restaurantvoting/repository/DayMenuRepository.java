package ru.javaops.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.javaops.restaurantvoting.repository.model.DayMenu;
import ru.javaops.restaurantvoting.repository.model.Restaurant;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface DayMenuRepository extends BaseRepository<DayMenu>, JpaSpecificationExecutor<DayMenu> {

    Boolean existsByMenuDateAndRestaurant(LocalDate menuDate, Restaurant restaurant);

    @Query("SELECT dm FROM DayMenu dm JOIN FETCH dm.meals JOIN FETCH dm.restaurant")
    List<DayMenu> getAllWithMeals();

    @Query("SELECT dm FROM DayMenu dm JOIN FETCH dm.meals JOIN FETCH dm.restaurant WHERE dm.menuDate = :menuDate")
    List<DayMenu> getAllWithMealsByDate(LocalDate menuDate);

    @Modifying
    @Query(value = "UPDATE day_menu dm set dm.votes_counter = dm.votes_counter + 1 WHERE dm.id = :id", nativeQuery = true)
    int incrementVotesCounter(UUID id);

    @Modifying
    @Query(value = "UPDATE day_menu dm set dm.votes_counter = dm.votes_counter - 1 WHERE dm.id = :id", nativeQuery = true)
    int decrementVotesCounter(UUID id);


}
