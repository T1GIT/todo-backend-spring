package com.todo.app.security.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class InvalidPswException extends SecurityException {
    public InvalidPswException(String psw) {
        super("Invalid password %s"
                .formatted(psw));
    }
}
