package ru.javaops.restaurantvoting.error;

public class DataConflictException extends AppException {
    public DataConflictException(String msg) {
        super(msg);
    }
}