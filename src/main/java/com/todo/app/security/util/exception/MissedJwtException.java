package com.todo.app.security.util.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception throwing if JWT was missed in the cookie
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class MissedJwtException extends SecurityException {
    public MissedJwtException() {
        super("Jwt not found in the cookies");
    }
}
