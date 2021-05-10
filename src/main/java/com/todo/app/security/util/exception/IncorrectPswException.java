package com.todo.app.security.util.exception;

import com.todo.app.data.util.base.DataException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "INVALID_PSW")
public class IncorrectPswException extends SecurityException {
    public IncorrectPswException(String email, String psw) {
        super(String.format(
                "Password %s is incorrect for user with email %s",
                psw, email));
    }
}
