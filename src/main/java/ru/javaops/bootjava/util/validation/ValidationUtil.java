package ru.javaops.bootjava.util.validation;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.springframework.core.NestedExceptionUtils;
import ru.javaops.bootjava.HasId;
import ru.javaops.bootjava.error.IllegalRequestDataException;
import ru.javaops.bootjava.error.NotFoundException;

import java.util.UUID;

@UtilityClass
public class ValidationUtil {

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must be new (id=null)");
        }
    }

    //  Conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
    public static void assureIdConsistent(HasId bean, UUID id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must has id=" + id);
        }
    }

    public static void checkModification(int count, UUID id) {
        if (count == 0) {
            throw new IllegalRequestDataException("Entity with id=" + id + " not found");
        }
    }
    public static <T> T checkExisted(T obj, UUID id) {
        if (obj == null) {
            throw new NotFoundException("Entity with id=" + id + " not found");
        }
        return obj;
    }

    //  https://stackoverflow.com/a/65442410/548473
    @NonNull
    public static Throwable getRootCause(@NonNull Throwable t) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        return rootCause != null ? rootCause : t;
    }
}