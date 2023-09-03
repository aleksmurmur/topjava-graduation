package ru.javaops.bootjava.web;

import ru.javaops.bootjava.repository.model.Restaurant;
import ru.javaops.bootjava.repository.model.Role;
import ru.javaops.bootjava.repository.model.User;
import ru.javaops.bootjava.to.RestaurantCreateOrUpdateRequest;
import ru.javaops.bootjava.to.RestaurantResponse;
import ru.javaops.bootjava.to.UserCreateOrUpdateRequest;

import java.util.Date;
import java.util.Random;
import java.util.Set;

public class DataGenerator {

    public static final String ADMIN_EMAIL = "admin@gmail.com";
    public static final String GUEST_EMAIL = "guest@gmail.com";

    public static User admin() {
        return new User("admin", ADMIN_EMAIL, "admin", Set.of(Role.ADMIN, Role.USER));
    }

    public static User guest() {
        return new User("guest", GUEST_EMAIL, "guest", Set.of(Role.USER));
    }
    public static RestaurantCreateOrUpdateRequest restaurantCreateOrUpdateRequest() {
        return new RestaurantCreateOrUpdateRequest(
                TestUtils.randomString()
        );
    }

    public static Restaurant restaurant() {
        return new Restaurant(
                TestUtils.randomString()
        );
    }
    public static UserCreateOrUpdateRequest userCreateOrUpdateRequest() {
        return userCreateOrUpdateRequest(TestUtils.randomEmail());
    }

    public static UserCreateOrUpdateRequest userCreateOrUpdateRequest(String email) {
        return new UserCreateOrUpdateRequest(
                TestUtils.randomString(),
                email,
                TestUtils.randomString()
        );
    }

    public static User user() {
        return new User(
                null,
                TestUtils.randomString(),
                TestUtils.randomEmail(),
                TestUtils.randomString(),
                new Random().nextBoolean(),
                new Date(),
                TestUtils.randomRoles()
        );
    }

}
