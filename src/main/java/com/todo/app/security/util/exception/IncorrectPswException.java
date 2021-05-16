package com.todo.app.security.util.exception;

import com.todo.app.data.util.base.DataException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class IncorrectPswException extends SecurityException {
    public IncorrectPswException(String email) {
        super(String.format(
                "Incorrect password for user with email %s",
                email));
    }
}
