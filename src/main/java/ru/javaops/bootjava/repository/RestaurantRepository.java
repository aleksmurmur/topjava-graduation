package ru.javaops.bootjava.repository;

import org.springframework.transaction.annotation.Transactional;
import ru.javaops.bootjava.repository.model.Restaurant;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {


}