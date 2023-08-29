package ru.javaops.bootjava.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@UtilityClass
public class WebUtil {

    public static URI getEntityUri (UUID id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
    }
}
