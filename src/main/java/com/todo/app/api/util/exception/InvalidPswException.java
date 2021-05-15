package com.todo.app.api.util.exception;

import com.todo.app.api.util.base.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "INVALID_PSW")
public class InvalidPswException extends ApiException {
        public InvalidPswException(String psw) {
            super(String.format("Password %s is invalid",
                    psw));
        }
}
