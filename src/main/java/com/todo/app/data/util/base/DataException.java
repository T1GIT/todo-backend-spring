package com.todo.app.data.util.base;

public abstract class DataException extends RuntimeException {
    public DataException(String msg) {
        super(msg);
    }
}
