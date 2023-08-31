package ru.javaops.bootjava.util;

import lombok.experimental.UtilityClass;
import ru.javaops.bootjava.repository.model.Role;
import ru.javaops.bootjava.repository.model.User;
import ru.javaops.bootjava.to.UserTo;

import java.util.Set;

import static ru.javaops.bootjava.config.SecurityConfig.PASSWORD_ENCODER;

@UtilityClass
public class UserUtil {

    public static User createNewFromTo(UserTo userTo) {
        return new User(null, userTo.getName(), userTo.getEmail().toLowerCase(), userTo.getPassword(), Set.of(Role.USER));
    }

    public static User updateFromTo(User user, UserTo userTo) {
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setPassword(userTo.getPassword());
        return user;
    }

    public static User prepareToSave(User user) {
        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }
}