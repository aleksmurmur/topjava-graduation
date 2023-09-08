package ru.javaops.restaurantvoting.web;

import ru.javaops.restaurantvoting.repository.model.*;
import ru.javaops.restaurantvoting.to.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import static ru.javaops.restaurantvoting.web.TestUtils.*;

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
                randomString()
        );
    }

    public static Restaurant restaurant() {
        return new Restaurant(
                randomString()
        );
    }
    public static UserCreateOrUpdateRequest userCreateOrUpdateRequest() {
        return userCreateOrUpdateRequest(TestUtils.randomEmail());
    }

    public static UserCreateOrUpdateRequest userCreateOrUpdateRequest(String email) {
        return new UserCreateOrUpdateRequest(
                randomString(),
                email,
                randomString()
        );
    }

    public static User user() {
        return new User(
                null,
                randomString(),
                randomEmail(),
                randomString(),
                new Random().nextBoolean(),
                new Date(),
                randomRoles()
        );
    }

    public static MealCreateOrUpdateRequest mealCreateOrUpdateRequest(UUID restaurantId) {
        return new MealCreateOrUpdateRequest(
                randomString(),
                randomPrice(),
                restaurantId
        );
    }

    public static Meal meal(Restaurant restaurant) {
        return new Meal(
                randomString(),
                randomPrice(),
                restaurant
        );
    }

    public static DayMenuCreateOrUpdateRequest dayMenuCreateOrUpdateRequest(Set<UUID> mealIds, UUID restaurantId) {
        return new DayMenuCreateOrUpdateRequest(
                LocalDate.now().plusDays(new Random().nextInt(0, 10)),
                mealIds,
                restaurantId
        );
    }



}
