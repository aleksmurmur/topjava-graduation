package ru.javaops.restaurantvoting.repository;

import org.springframework.data.jpa.repository.Query;
import ru.javaops.restaurantvoting.error.NotFoundException;
import ru.javaops.restaurantvoting.repository.model.User;

import java.util.Optional;

public interface UserRepository extends BaseRepository<User> {

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE upper(u.email) = upper(:email)")
    Optional<User> findByEmailIgnoreCase(String email);

    default User getByEmail(String email) {
        return findByEmailIgnoreCase(email).orElseThrow(() -> new NotFoundException("User was not found by email = " + email));
    }
}