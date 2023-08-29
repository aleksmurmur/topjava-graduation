package ru.javaops.bootjava.web.user;

import ru.javaops.bootjava.repository.model.Role;
import ru.javaops.bootjava.repository.model.User;
import ru.javaops.bootjava.util.JsonUtil;
import ru.javaops.bootjava.web.MatcherFactory;

import java.util.Collections;
import java.util.Date;
import java.util.UUID;

public class UserTestData {
    public static final MatcherFactory.Matcher<User> USER_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(User.class, "registered", "password");

    public static final UUID USER_ID = UUID.randomUUID();
    public static final UUID ADMIN_ID = UUID.randomUUID();
    public static final UUID GUEST_ID = UUID.randomUUID();
    public static final UUID NOT_FOUND = UUID.randomUUID();
    public static final String USER_MAIL = "2user@yandex.ru";
    public static final String ADMIN_MAIL = "admin@gmail.com";
    public static final String GUEST_MAIL = "guest@gmail.com";

    public static final User user = new User(null, "User", USER_MAIL, "password", Role.USER);
    public static final User admin = new User(null, "Admin", ADMIN_MAIL, "admin", Role.ADMIN, Role.USER);
    public static final User guest = new User(null, "Guest", GUEST_MAIL, "guest");



    public static User getNew() {
        return new User(null, "New", "new@gmail.com", "newPass", false, new Date(), Collections.singleton(Role.USER));
    }

    public static User getUpdated() {
        return new User(USER_ID, "UpdatedName", USER_MAIL, "newPass", false, new Date(), Collections.singleton(Role.ADMIN));
    }

    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }
}
