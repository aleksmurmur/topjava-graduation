package ru.javaops.bootjava.web.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.bootjava.repository.model.Role;
import ru.javaops.bootjava.repository.model.User;
import ru.javaops.bootjava.repository.UserRepository;
import ru.javaops.bootjava.to.UserCreateOrUpdateRequest;
import ru.javaops.bootjava.to.UserResponse;
import ru.javaops.bootjava.web.AbstractControllerTest;

import java.util.Comparator;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.in;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.bootjava.web.DataGenerator.*;
import static ru.javaops.bootjava.web.user.AdminUserController.REST_URL;
import static ru.javaops.bootjava.web.Matchers.*;

class AdminUserControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = REST_URL + '/';

    private User user1;

    @BeforeEach
    private void before() {
        setDefaultUsers();
        user1 = userRepository.save(user());
    }

    @AfterEach
    private void after() {
        cleanDb();
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + admin.id()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_RESPONSE_MATCHER.contentJson(admin));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + UUID.randomUUID()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void getByEmail() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "by-email?email=" + ADMIN_EMAIL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_RESPONSE_MATCHER.contentJson(admin));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + user1.id()))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(userRepository.existsById(user1.id()));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + UUID.randomUUID()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void enableNotFound() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL_SLASH + UUID.randomUUID())
                .param("enabled", "false")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = GUEST_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void update() throws Exception {
        UserCreateOrUpdateRequest request = userCreateOrUpdateRequest();
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + user1.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(USER_REQUEST_MATCHER.contentJson(request));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void createWithLocation() throws Exception {
        UserCreateOrUpdateRequest request = userCreateOrUpdateRequest();
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(USER_REQUEST_MATCHER.contentJson(request));

    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_RESPONSE_MATCHER.contentJson(Stream.of(admin, guest, user1).sorted(
                        Comparator.comparing(User::getName)
                ).toList()));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void enable() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL_SLASH + user1.id())
                .param("enabled", "false")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertFalse(userRepository.findByIdOrThrow(user1.id()).isEnabled());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void createInvalid() throws Exception {
        UserCreateOrUpdateRequest invalidRequest = new UserCreateOrUpdateRequest( null, "", "newPass", Set.of(Role.USER, Role.ADMIN));
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidRequest)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void updateInvalid() throws Exception {
        UserCreateOrUpdateRequest invalidRequest = new UserCreateOrUpdateRequest( "", user1.getEmail(), user1.getPassword(), user1.getRoles());
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + user1.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidRequest)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void updateHtmlUnsafe() throws Exception {
        UserCreateOrUpdateRequest invalidRequest = new UserCreateOrUpdateRequest( "<script>alert(123)</script>", user1.getEmail(), user1.getPassword(), user1.getRoles());
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + user1.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidRequest)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void updateDuplicate() throws Exception {
        UserCreateOrUpdateRequest invalidRequest = new UserCreateOrUpdateRequest( user1.getName(), ADMIN_EMAIL, user1.getPassword(), user1.getRoles());
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + user1.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidRequest)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(UniqueMailValidator.EXCEPTION_DUPLICATE_EMAIL)));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void createDuplicate() throws Exception {
        UserCreateOrUpdateRequest invalidRequest = userCreateOrUpdateRequest(GUEST_EMAIL);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidRequest)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(UniqueMailValidator.EXCEPTION_DUPLICATE_EMAIL)));
    }
}