package com.todo.app.data.service;

import com.todo.app.data.model.Refresh;
import com.todo.app.data.util.exception.ResourceNotFoundException;

public interface RefreshService {

    Refresh create(long userId) throws ResourceNotFoundException;

    Refresh update(String value) throws ResourceNotFoundException;

    void delete(String value) throws ResourceNotFoundException;
}
