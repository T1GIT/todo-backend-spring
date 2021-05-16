package com.todo.app.security.util.exception;


/**
 * Exception throwing if refresh token was missed
 * in the cookie or in the database
 */
public class MissedRefreshException extends SecurityException {
    public MissedRefreshException() {
        super("Refresh token not found in the cookies");
    }
}
