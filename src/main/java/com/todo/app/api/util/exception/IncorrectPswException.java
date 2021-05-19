package com.todo.app.api.util.exception;

import com.todo.app.api.util.base.IncorrectFormException;


public class IncorrectPswException extends IncorrectFormException {
    public IncorrectPswException(String psw) {
        super("Password", psw);
    }
}
