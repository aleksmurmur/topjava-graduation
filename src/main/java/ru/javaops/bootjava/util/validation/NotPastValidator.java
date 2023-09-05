package ru.javaops.bootjava.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import java.time.LocalDate;

public class NotPastValidator implements ConstraintValidator<NotPast, LocalDate> {
    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext ctx) {
        return value == null || !LocalDate.now().isAfter(value);
    }
}
