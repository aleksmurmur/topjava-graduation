package ru.javaops.bootjava.to;

public record NamedElement<T>(
    T id,
    String name
) {
}
