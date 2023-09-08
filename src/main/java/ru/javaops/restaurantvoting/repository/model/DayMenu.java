package ru.javaops.restaurantvoting.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.javaops.restaurantvoting.HasId;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "day_menu", indexes = @Index(name = "idx_name", columnList = "menu_date", unique = false))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DayMenu extends BaseEntity implements HasId {

    @Column(name = "menu_date", nullable = false, columnDefinition = "date")
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate menuDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "day_menu_meal",
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

    @Column(name = "votes_counter", nullable = false)
    private int votesCounter;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dayMenu")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Vote> votes;

    public DayMenu(LocalDate menuDate, Set<Meal> meals, Restaurant restaurant) {
        this.menuDate = menuDate;
        this.meals = meals;
        this.restaurant = restaurant;
        this.votesCounter = 0;
        this.votes = new HashSet<>();
    }

    public Boolean addVote(Vote vote) {
        return this.votes.add(vote);
    }

    @Override
    public String toString() {
        return "DayMenu{" +
                "id=" + id +
                ", menuDate=" + menuDate +
                ", meals=" + meals +
                ", restaurant=" + restaurant +
                ", votesCounter=" + votesCounter +
                '}';
    }


}
