package com.todo.app.data.util.exception;

import com.todo.app.data.util.base.DataException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class EmailNotExistsException extends DataException {
    public EmailNotExistsException(String email) {
        super(String.format(
                "User with email %s not found",
                email));
    }
}
