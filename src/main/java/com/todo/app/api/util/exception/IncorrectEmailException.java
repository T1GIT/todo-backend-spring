package com.todo.app.api.util.exception;


import com.todo.app.api.util.base.IncorrectFormException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class IncorrectEmailException extends IncorrectFormException {
    public IncorrectEmailException(String email) {
        super("Email", email);
    }
}
