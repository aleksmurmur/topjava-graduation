package ru.javaops.bootjava.web.meal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import ru.javaops.bootjava.repository.model.Meal;
import ru.javaops.bootjava.repository.model.Restaurant;
import ru.javaops.bootjava.to.MealCreateOrUpdateRequest;
import ru.javaops.bootjava.web.AbstractControllerTest;

import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.bootjava.web.DataGenerator.*;
import static ru.javaops.bootjava.web.Matchers.*;

class MealControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = MealController.REST_URL + '/';

    private Meal meal1;
    private Meal meal2;
    private Restaurant restaurant;


    @BeforeEach
    void before() {
        cleanDb();
        setDefaultUsers();
        restaurant = restaurantRepository.save(restaurant());
        meal1 = mealRepository.save(meal(restaurant));
        meal2 = mealRepository.save(meal(restaurant));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void getById() throws Exception {
        perform(get(REST_URL_SLASH + meal1.id()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MEAL_RESPONSE_MATCHER.contentJson(meal1));
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
        perform(delete(REST_URL_SLASH + meal1.id()))
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
        perform(get(MealController.REST_URL))
                .andExpect(status().isOk())
                .andExpect(MEAL_RESPONSE_MATCHER.contentJson(Stream.of(meal1, meal2).sorted(
                        Comparator.comparing(Meal::id)
                ).toList()));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void createWithLocation() throws Exception {
        MealCreateOrUpdateRequest request = mealCreateOrUpdateRequest(restaurant.id());
        perform(post(MealController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MEAL_REQUEST_MATCHER.contentJson(request));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void createInvalid() throws Exception {
        MealCreateOrUpdateRequest request = new MealCreateOrUpdateRequest(null, 0, null);
        perform(post(MealController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void update() throws Exception {
        MealCreateOrUpdateRequest request = mealCreateOrUpdateRequest(restaurant.id());
        perform(put(REST_URL_SLASH + meal1.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(MEAL_REQUEST_MATCHER.contentJson(request));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void updateInvalid() throws Exception {
        MealCreateOrUpdateRequest request = new MealCreateOrUpdateRequest(meal1.getName(), -1, meal1.getRestaurant().id());
        perform(put(REST_URL_SLASH + meal1.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());
    }
}