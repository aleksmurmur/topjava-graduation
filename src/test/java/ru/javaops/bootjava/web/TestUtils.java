package ru.javaops.bootjava.web;

import org.apache.commons.lang3.RandomStringUtils;
import ru.javaops.bootjava.repository.model.Role;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TestUtils {

    private static final Random random = new Random();


    public static String randomString() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    public static String randomEmail() {
        return String.format("%s@%s.%s",
                RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphanumeric(5),
                RandomStringUtils.randomAlphabetic(3)
        ).toLowerCase();
    }

    public static Set<Role> randomRoles() {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        Role[] values = Arrays.stream(Role.class.getEnumConstants()).filter(Predicate.not(Role.USER::equals)).toArray(Role[]::new);
        for (int i = 0; i < random.nextInt(values.length + 1); i++) {
            roles.add(values[random.nextInt(values.length)]);
        }
        return roles;
    }

    public static long randomPrice() {
        return random.nextLong(0, Long.MAX_VALUE);
    }

}
