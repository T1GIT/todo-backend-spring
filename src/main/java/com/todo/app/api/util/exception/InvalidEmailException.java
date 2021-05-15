package com.todo.app.api.util.exception;


import com.todo.app.api.util.base.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "INVALID_EMAIL")
public class InvalidEmailException extends ApiException {
    public InvalidEmailException(String email) {
        super(String.format("Email %s is invalid",
                email));
    }
}
