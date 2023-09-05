package ru.javaops.bootjava.web;

import ru.javaops.bootjava.repository.model.DayMenu;
import ru.javaops.bootjava.repository.model.Meal;
import ru.javaops.bootjava.repository.model.Restaurant;
import ru.javaops.bootjava.repository.model.User;
import ru.javaops.bootjava.to.*;

public class Matchers {
    public static final MatcherFactory.Matcher<User, UserResponse> USER_RESPONSE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(User.class, UserResponse.class, "password");
    public static final MatcherFactory.Matcher<User, User> USER_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(User.class, User.class, "password");
    public static final MatcherFactory.Matcher<UserCreateOrUpdateRequest, UserResponse> USER_REQUEST_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(UserCreateOrUpdateRequest.class, UserResponse.class, "id", "roles");

    public static final MatcherFactory.Matcher<Restaurant, RestaurantResponse> RESTAURANT_RESPONSE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, RestaurantResponse.class);

    public static final MatcherFactory.Matcher<RestaurantCreateOrUpdateRequest, Restaurant> RESTAURANT_REQUEST_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(RestaurantCreateOrUpdateRequest.class, Restaurant.class, "id");
    public static final MatcherFactory.Matcher<Meal, MealResponse> MEAL_RESPONSE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Meal.class, MealResponse.class);
    public static final MatcherFactory.Matcher<MealCreateOrUpdateRequest, Meal> MEAL_REQUEST_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(MealCreateOrUpdateRequest.class, Meal.class, "id", "restaurant");
    public static final MatcherFactory.Matcher<DayMenu, DayMenuResponse> DAY_MENU_RESPONSE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(DayMenu.class, DayMenuResponse.class, "votes", "meals");
    public static final MatcherFactory.Matcher<DayMenuCreateOrUpdateRequest, DayMenu> DAY_MENU_REQUEST_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(DayMenuCreateOrUpdateRequest.class, DayMenu.class, "id");

}
