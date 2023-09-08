package ru.javaops.restaurantvoting.web.daymenu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import ru.javaops.restaurantvoting.repository.model.DayMenu;
import ru.javaops.restaurantvoting.repository.model.Meal;
import ru.javaops.restaurantvoting.repository.model.Restaurant;
import ru.javaops.restaurantvoting.to.DayMenuResponse;
import ru.javaops.restaurantvoting.util.JsonUtil;
import ru.javaops.restaurantvoting.web.AbstractControllerTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.restaurantvoting.web.DataGenerator.*;
import static ru.javaops.restaurantvoting.web.Matchers.DAY_MENU_RESPONSE_MATCHER;

class DayMenuUserControllerTest extends AbstractControllerTest {
    private static final String REST_URL_SLASH = DayMenuUserController.REST_URL + '/';
    private Restaurant restaurant;
    private Meal meal1;
    private Meal meal2;
    private DayMenu dayMenu1;
    private Restaurant restaurant2;
    private Meal meal3;

    private DayMenu dayMenu2;

    @BeforeEach
    void before() {
        cleanDb();
        setDefaultUsers();
        restaurant = restaurantRepository.save(restaurant());
        meal1 = mealRepository.save(meal(restaurant));
        meal2 = mealRepository.save(meal(restaurant));
        dayMenu1 = dayMenuRepository.save(new DayMenu(LocalDate.now(), Set.of(meal1, meal2), restaurant));
        restaurant2 = restaurantRepository.save(restaurant());
        meal3 = mealRepository.save(meal(restaurant2));
        dayMenu2 = dayMenuRepository.save(new DayMenu(LocalDate.now(), Set.of(meal3), restaurant2));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void getAll() throws Exception {
        String resultString = perform(get(DayMenuUserController.REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        List<DayMenuResponse> dayMenusResponse = JsonUtil.readValues(resultString, DayMenuResponse.class);
        assertEquals(2, dayMenusResponse.size());
        DayMenuResponse dayMenu1Response = dayMenusResponse.stream().filter(dm -> dm.id().equals(dayMenu1.id())).findAny().orElse(null);
        assertEquals(dayMenu1.id(), dayMenu1Response.id());
        assertEquals(0 ,dayMenu1Response.votes());
        assertEquals(dayMenu1.getMeals().size(), dayMenu1Response.meals().size());
    }


    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void vote() throws Exception {
        String resultString = perform(put(REST_URL_SLASH + dayMenu1.id()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(DAY_MENU_RESPONSE_MATCHER.contentJson(dayMenu1))
                .andReturn().getResponse().getContentAsString();

        DayMenuResponse response = JsonUtil.readValue(resultString, DayMenuResponse.class);
        assertEquals(dayMenu1.getMeals().size(), response.meals().size());
        //workaround for tests because of mix of jpa and native queries
        entityManager.clear();
        assertEquals(1, dayMenuRepository.findByIdOrThrow(dayMenu1.id()).getVotesCounter());
    }
}