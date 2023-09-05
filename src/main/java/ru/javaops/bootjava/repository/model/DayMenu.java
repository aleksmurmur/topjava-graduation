package ru.javaops.bootjava.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.util.CollectionUtils;
import ru.javaops.bootjava.HasId;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "day_menus")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DayMenu extends BaseEntity implements HasId {

    @Column(name = "menu_date", nullable = false, columnDefinition = "date")
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate menuDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "day_menus_meals",
            joinColumns = { @JoinColumn(name = "day_menu_id") },
            inverseJoinColumns = { @JoinColumn(name = "meal_id") })
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Meal> meals;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    @NotNull
    private Restaurant restaurant;

    private int votesCounter;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Vote> votes;

    public DayMenu(LocalDate menuDate, Set<Meal> meals, Restaurant restaurant) {
        this.menuDate = menuDate;
        this.meals = meals;
        this.restaurant = restaurant;
        this.votesCounter = 0;
        this.votes = Set.of();
    }

    public Boolean addVote(Vote vote) {
        return this.votes.add(vote);
    }


}
