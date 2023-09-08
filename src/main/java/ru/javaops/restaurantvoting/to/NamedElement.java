package ru.javaops.restaurantvoting.to;

public record NamedElement<T>(
    T id,
    String name
) {
}
