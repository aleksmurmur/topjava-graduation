package ru.javaops.bootjava.web;

import ru.javaops.bootjava.repository.model.Restaurant;
import ru.javaops.bootjava.repository.model.User;
import ru.javaops.bootjava.to.RestaurantCreateOrUpdateRequest;
import ru.javaops.bootjava.to.RestaurantResponse;
import ru.javaops.bootjava.to.UserCreateOrUpdateRequest;
import ru.javaops.bootjava.to.UserResponse;

public class Matchers {
    public static final MatcherFactory.Matcher<User, UserResponse> USER_RESPONSE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(User.class, UserResponse.class, "password");
    public static final MatcherFactory.Matcher<User, User> USER_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(User.class, User.class, "password");
    public static final MatcherFactory.Matcher<UserCreateOrUpdateRequest, UserResponse> USER_REQUEST_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(UserCreateOrUpdateRequest.class, UserResponse.class, "id", "roles");

    public static final MatcherFactory.Matcher<Restaurant, RestaurantResponse> RESTAURANT_RESPONSE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, RestaurantResponse.class);

    public static final MatcherFactory.Matcher<RestaurantCreateOrUpdateRequest, Restaurant> RESTAURANT_REQUEST_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(RestaurantCreateOrUpdateRequest.class, Restaurant.class, "id");

}
