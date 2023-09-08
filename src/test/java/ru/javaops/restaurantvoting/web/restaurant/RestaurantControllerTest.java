package ru.javaops.restaurantvoting.web.restaurant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import ru.javaops.restaurantvoting.repository.model.Restaurant;
import ru.javaops.restaurantvoting.to.RestaurantCreateOrUpdateRequest;
import ru.javaops.restaurantvoting.web.AbstractControllerTest;

import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Stream;

import static ru.javaops.restaurantvoting.web.DataGenerator.*;
import static ru.javaops.restaurantvoting.web.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static ru.javaops.restaurantvoting.web.restaurant.RestaurantController.REST_URL;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


class RestaurantControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = RestaurantController.REST_URL + '/';

    private Restaurant restaurant1;
    private Restaurant restaurant2;


    @BeforeEach
    void before() {
        cleanDb();
        setDefaultUsers();
        restaurant1 = restaurantRepository.save(restaurant());
        restaurant2 = restaurantRepository.save(restaurant());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void getById() throws Exception {
        perform(get(REST_URL_SLASH + restaurant1.id()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(RESTAURANT_RESPONSE_MATCHER.contentJson(restaurant1));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void getNotFound() throws Exception {
        perform(get(REST_URL_SLASH + UUID.randomUUID()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deleteById() throws Exception {
        perform(delete(REST_URL_SLASH + restaurant1.id()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deleteNotFound() throws Exception {
        perform(delete(REST_URL_SLASH + UUID.randomUUID()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void getAll() throws Exception {
        perform(get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(RESTAURANT_RESPONSE_MATCHER.contentJson(Stream.of(restaurant1, restaurant2).sorted(
                        Comparator.comparing(Restaurant::getName)
                ).toList()));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void createWithLocation() throws Exception {
        RestaurantCreateOrUpdateRequest request = restaurantCreateOrUpdateRequest();
        perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(RESTAURANT_REQUEST_MATCHER.contentJson(request));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void createInvalid() throws Exception {
        RestaurantCreateOrUpdateRequest request = new RestaurantCreateOrUpdateRequest(null);
        perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void update() throws Exception {
        RestaurantCreateOrUpdateRequest request = restaurantCreateOrUpdateRequest();
        perform(put(REST_URL_SLASH + restaurant1.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(RESTAURANT_REQUEST_MATCHER.contentJson(request));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void updateInvalid() throws Exception {
        RestaurantCreateOrUpdateRequest request = new RestaurantCreateOrUpdateRequest("");
        perform(put(REST_URL_SLASH + restaurant1.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());
    }
}