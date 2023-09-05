package ru.javaops.bootjava.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.javaops.bootjava.repository.model.DayMenu;
import ru.javaops.bootjava.repository.model.Restaurant;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface DayMenuRepository extends BaseRepository<DayMenu>, JpaSpecificationExecutor<DayMenu> {

    Boolean existsByDateAndRestaurant(LocalDate date, Restaurant restaurant);

    @Query("SELECT dm FROM DayMenu dm JOIN FETCH dm.meals JOIN FETCH dm.restaurant")
    List<DayMenu> getAllWithMeals();

    @Query("SELECT dm FROM DayMenu dm JOIN FETCH dm.meals JOIN FETCH dm.restaurant WHERE dm.date = :date")
    List<DayMenu> getAllWithMealsByDate(LocalDate date);

    @Modifying
    @Query(value = "UPDATE day_menus dm set dm.votes_counter = dm.votes_counter + 1 WHERE dm.id = :id", nativeQuery = true)
    int incrementVotesCounter(UUID id);

    @Modifying
    @Query(value = "UPDATE day_menus dm set dm.votes_counter = dm.votes_counter - 1 WHERE dm.id = :id", nativeQuery = true)
    int decrementVotesCounter(UUID id);


}
