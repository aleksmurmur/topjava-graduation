package ru.javaops.bootjava.repository;

import ru.javaops.bootjava.repository.model.User;
import ru.javaops.bootjava.repository.model.Vote;

import java.time.LocalDate;
import java.util.Optional;

public interface VoteRepository extends BaseRepository<Vote> {

    Optional<Vote> findByCreatedAndUser(LocalDate created, User user);
}
