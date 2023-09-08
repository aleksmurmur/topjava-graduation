package ru.javaops.restaurantvoting.repository;

import ru.javaops.restaurantvoting.repository.model.User;
import ru.javaops.restaurantvoting.repository.model.Vote;

import java.time.LocalDate;
import java.util.Optional;

public interface VoteRepository extends BaseRepository<Vote> {

    Optional<Vote> findByCreatedAndUser(LocalDate created, User user);
}
