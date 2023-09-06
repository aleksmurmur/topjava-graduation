package ru.javaops.bootjava.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.bootjava.repository.DayMenuRepository;
import ru.javaops.bootjava.repository.MealRepository;
import ru.javaops.bootjava.repository.RestaurantRepository;
import ru.javaops.bootjava.repository.UserRepository;
import ru.javaops.bootjava.repository.model.User;


//https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-testing-spring-boot-applications
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@ActiveProfiles("test")
//@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})

public abstract class AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected ObjectMapper mapper;

    protected User admin;
    protected User guest;


    @Autowired
    protected RestaurantRepository restaurantRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected MealRepository mealRepository;
    @Autowired
    protected DayMenuRepository dayMenuRepository;

    protected ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {
        return mockMvc.perform(builder);
    }

    protected void cleanDb() {
        restaurantRepository.deleteAll();
        userRepository.deleteAll();
        mealRepository.deleteAll();
        dayMenuRepository.deleteAll();
    }

    public void setDefaultUsers(){
        admin = userRepository.save(DataGenerator.admin());
        guest = userRepository.save(DataGenerator.guest());
    }
}
