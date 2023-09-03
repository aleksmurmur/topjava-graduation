package ru.javaops.bootjava.web.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.bootjava.repository.model.Role;
import ru.javaops.bootjava.repository.model.User;
import ru.javaops.bootjava.to.UserCreateOrUpdateRequest;
import ru.javaops.bootjava.util.JsonUtil;
import ru.javaops.bootjava.web.AbstractControllerTest;

import java.util.Set;

import static ru.javaops.bootjava.web.DataGenerator.*;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static ru.javaops.bootjava.web.user.ProfileController.REST_URL;
import static ru.javaops.bootjava.web.Matchers.*;


class ProfileControllerTest extends AbstractControllerTest {

    private User user1;

    @BeforeEach
     protected void before() {
        cleanDb();
        setDefaultUsers();
         user1 = userRepository.save(user());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void getProfile() throws Exception {
        perform(get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_RESPONSE_MATCHER.contentJson(admin));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = GUEST_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deleteProfile() throws Exception {
        perform(delete(REST_URL))
                .andExpect(status().isNoContent());
        USER_MATCHER.assertMatch(userRepository.findAll(), admin, user1);
    }

    @Test
    void register() throws Exception {
        UserCreateOrUpdateRequest request = userCreateOrUpdateRequest();
        perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(USER_REQUEST_MATCHER.contentJson(request));
    }

    @Test
    @WithUserDetails(value = GUEST_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void update() throws Exception {
        UserCreateOrUpdateRequest request = userCreateOrUpdateRequest();
        perform(put(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(USER_REQUEST_MATCHER.contentJson(request));
    }

    @Test
    void registerInvalid() throws Exception {
        UserCreateOrUpdateRequest invalidRequest = new UserCreateOrUpdateRequest(null, null, null, null);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidRequest)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = GUEST_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void updateInvalid() throws Exception {
        UserCreateOrUpdateRequest request = new UserCreateOrUpdateRequest(null, null, "password", null);
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = GUEST_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void updateDuplicate() throws Exception {
        UserCreateOrUpdateRequest request = new UserCreateOrUpdateRequest( "newName", ADMIN_EMAIL, "newPassword", Set.of(Role.USER));
        perform(MockMvcRequestBuilders.put(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(request)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(UniqueMailValidator.EXCEPTION_DUPLICATE_EMAIL)));
    }
}