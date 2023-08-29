package ru.javaops.bootjava;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.Assert;

import java.util.UUID;

public interface HasId {
    UUID getId();

    void setId(UUID id);

    @JsonIgnore
    default boolean isNew() {
        return getId() == null;
    }

    // doesn't work for hibernate lazy proxy
    default UUID id() {
        Assert.notNull(getId(), "Entity must has id");
        return getId();
    }
}
