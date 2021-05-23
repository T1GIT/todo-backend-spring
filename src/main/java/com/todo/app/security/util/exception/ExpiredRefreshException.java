package com.todo.app.security.util.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception throwing if refresh token is expired
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class ExpiredRefreshException extends SecurityException {
    public ExpiredRefreshException(long sessionId, String refresh) {
        super("Refresh token %s from session with id %d is expired"
                .formatted(refresh, sessionId));
    }
}
