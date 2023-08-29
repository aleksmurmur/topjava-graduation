package ru.javaops.bootjava.repository.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.javaops.bootjava.HasId;

import java.util.Date;

@Entity
@Table(name = "meals")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meal extends NamedEntity implements HasId {

    @Column(name = "price", nullable = false)
    private Long price;

    @ManyToOne
    private Restaurant restaurant;


}
