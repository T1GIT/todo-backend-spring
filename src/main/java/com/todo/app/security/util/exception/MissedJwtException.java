package com.todo.app.security.util.exception;


/**
 * Exception throwing if JWT was missed in the cookie
 */
public class MissedJwtException extends SecurityException {
    public MissedJwtException() {
        super("Jwt not found in the cookies");
    }
}
