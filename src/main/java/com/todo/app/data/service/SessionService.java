package com.todo.app.data.service;

import com.todo.app.data.model.Session;
import com.todo.app.data.util.exception.ResourceNotFoundException;
import com.todo.app.security.util.exception.ExpiredRefreshException;
import com.todo.app.security.util.exception.InvalidFingerprintException;

public interface SessionService {

    Session create(long userId, String fingerprint) throws ResourceNotFoundException;

    Session update(String refresh, String fingerprint)
            throws ResourceNotFoundException, InvalidFingerprintException, ExpiredRefreshException;

    void delete(String refresh);
}
