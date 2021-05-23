package com.todo.app.security.util.exception;

import com.todo.app.security.util.base.SecurityException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class AbsentAuthorisationException extends SecurityException {
    public AbsentAuthorisationException() {
        super("Authorisation context is absent in this mapping");
    }
}
