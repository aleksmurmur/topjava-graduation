package ru.javaops.bootjava.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.javaops.bootjava.HasId;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "day_menus")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DayMenu extends BaseEntity implements HasId {

    @Column(name = "date", nullable = false, columnDefinition = "timestamp")
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date date;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "day_menus_meals",
            joinColumns = { @JoinColumn(name = "day_menu_id") },
            inverseJoinColumns = { @JoinColumn(name = "meal_id") })
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
    private Set<Meal> meals;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    @NotNull
    private Restaurant restaurant;
}
