package com.todo.app.data.service;


import com.todo.app.data.model.Task;
import com.todo.app.data.util.exception.NotOwnerException;
import com.todo.app.data.util.exception.ResourceNotFoundException;

import java.util.List;


public interface TaskService {

    List<Task> getOf(long userId, long categoryId) throws ResourceNotFoundException, NotOwnerException;

    Task add(long userId, long categoryId, Task task) throws ResourceNotFoundException, NotOwnerException;

    void update(long userId, long taskId, Task newTask) throws ResourceNotFoundException, NotOwnerException;

    void setCompleted(long userId, long taskId, boolean isCompleted) throws ResourceNotFoundException, NotOwnerException;

    void delete(long userId, long taskId) throws ResourceNotFoundException, NotOwnerException;
}
