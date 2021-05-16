package com.todo.app.api.util.exception;


import com.todo.app.api.util.base.IncorrectFormException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class IncorrectFingerprintException extends IncorrectFormException {
    public IncorrectFingerprintException(String fingerprint) {
        super("Fingerprint", fingerprint);
    }
}
