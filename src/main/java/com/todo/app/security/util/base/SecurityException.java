package com.todo.app.security.util.base;

public abstract class SecurityException extends RuntimeException {
    public SecurityException(String msg) {
        super(msg);
    }
}
