package ru.javaops.bootjava.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Collection;

public class NotEmptyListCollection implements ConstraintValidator<NotEmptyCollection, Collection<?>> {
    @Override
    public boolean isValid(Collection<?> value, ConstraintValidatorContext ctx) {
        return value == null || !value.isEmpty();
    }
}
