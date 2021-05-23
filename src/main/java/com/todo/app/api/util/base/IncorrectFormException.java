package com.todo.app.api.util.base;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public abstract class IncorrectFormException extends ApiException {
    public IncorrectFormException(String field, String value) {
        super("%s %s is invalid".formatted(field, value));
    }
}
