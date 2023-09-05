package ru.javaops.bootjava.repository.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.javaops.bootjava.HasId;
import ru.javaops.bootjava.to.RestaurantResponse;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends NamedEntity implements HasId {

    public Restaurant(String name) {
        super(null, name);
    }

    public RestaurantResponse toResponse() {
        return new RestaurantResponse(
                id(),
                getName()
        );
    }
}
