package com.todo.app.api.util.exception;

import com.todo.app.api.util.base.IncorrectFormException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class IncorrectPswException extends IncorrectFormException {
    public IncorrectPswException(String psw) {
        super("Password", psw);
    }
}
