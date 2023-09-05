package ru.javaops.bootjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.javaops.bootjava.repository.model.User;
import ru.javaops.bootjava.repository.model.Vote;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface VoteRepository extends BaseRepository<Vote> {

    Optional<Vote> findByCreatedAndUser(LocalDate created, User user);
}
