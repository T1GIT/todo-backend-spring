package com.todo.app.data.util.exception;

import com.todo.app.data.util.base.DataException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.CONFLICT)
public class EmailExistsException extends DataException {
    public EmailExistsException(String email) {
        super(String.format(
                "User with email %s already contains",
                email));
    }
}
