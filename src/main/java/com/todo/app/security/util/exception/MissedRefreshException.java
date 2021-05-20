package com.todo.app.security.util.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception throwing if refresh token was missed
 * in the cookie or in the database
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class MissedRefreshException extends SecurityException {
    public MissedRefreshException() {
        super("Refresh token not found in the cookies");
    }
}
