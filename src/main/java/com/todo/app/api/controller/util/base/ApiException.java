package com.todo.app.api.controller.util.base;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public abstract class ApiException extends RuntimeException {
    public ApiException(String msg) {
        super(msg);
    }
}
