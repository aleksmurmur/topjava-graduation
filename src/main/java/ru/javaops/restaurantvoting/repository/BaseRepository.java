package ru.javaops.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import ru.javaops.restaurantvoting.error.NotFoundException;

import java.util.UUID;

@NoRepositoryBean
public interface BaseRepository<T> extends JpaRepository<T, UUID> {

    @Modifying
    @Query("DELETE FROM #{#entityName} e WHERE e.id=:id")
    int delete(UUID id);

    default void deleteExisted(UUID id) {
        if (delete(id) == 0) {
            throw new NotFoundException("Entity with id=" + id + " not found");
        }
    }


    default T findByIdOrThrow(UUID id) {
        return findById(id).orElseThrow(() -> new NotFoundException("Entity was not found by id " + id));
    }
}