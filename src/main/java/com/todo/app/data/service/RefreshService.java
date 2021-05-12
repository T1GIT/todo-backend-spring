package com.todo.app.data.service;

import com.todo.app.data.model.Refresh;
import com.todo.app.data.util.exception.ResourceNotFoundException;

public interface RefreshService {

    Refresh create(long userId) throws ResourceNotFoundException;

    boolean exists(long userId, String value) throws ResourceNotFoundException;

    Refresh update(long userId, String value) throws ResourceNotFoundException;

    void delete(long userId, String value) throws ResourceNotFoundException;
}
