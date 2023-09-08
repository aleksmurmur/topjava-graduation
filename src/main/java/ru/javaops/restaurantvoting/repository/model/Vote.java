package ru.javaops.restaurantvoting.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.javaops.restaurantvoting.HasId;

import java.time.LocalDate;

@Entity
@Table(name = "vote", indexes = {@Index(name = "idx_created_user_id_unq", columnList = "created, user_id", unique = true)})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote extends BaseEntity implements HasId {

    private LocalDate created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "day_menu_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    @NotNull
    private DayMenu dayMenu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    @NotNull
    private User user;

    public Vote(DayMenu dayMenu, User user) {
        this.created = LocalDate.now();
        this.dayMenu = dayMenu;
        this.user = user;
    }
}
