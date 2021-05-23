package com.todo.app.security.util.exception;

import com.todo.app.data.model.Session;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class InvalidFingerprintException extends SecurityException {
    public InvalidFingerprintException(Session session, String fingerprint) {
        super("Fingerprint %s not equal to fingerprint %s attached to session with refresh %s"
                .formatted(fingerprint, session.getFingerprint(), session.getRefresh()));
    }
}
