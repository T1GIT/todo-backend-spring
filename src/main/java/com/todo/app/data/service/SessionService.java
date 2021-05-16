package com.todo.app.data.service;

import com.todo.app.data.model.Session;
import com.todo.app.data.util.exception.ResourceNotFoundException;

public interface SessionService {

    Session create(long userId, String fingerprint) throws ResourceNotFoundException;

    Session update(String refresh, String fingerprint) throws ResourceNotFoundException;

    void delete(String refresh) throws ResourceNotFoundException;
}
