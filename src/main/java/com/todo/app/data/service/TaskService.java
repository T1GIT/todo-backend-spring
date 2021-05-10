package com.todo.app.data.service;


import com.todo.app.data.util.exception.ResourceNotFoundException;
import com.todo.app.data.model.Task;

import java.util.List;


public interface TaskService {

    List<Task> getOf(long categoryId) throws ResourceNotFoundException;

    Task add(long categoryId, Task task) throws ResourceNotFoundException;

    Task update(long taskId, Task newTask) throws ResourceNotFoundException;

    Task setCompleted(long taskId, Task newTask) throws ResourceNotFoundException;

    Task changeCategory(long taskId, long newCategoryId) throws ResourceNotFoundException;

    void delete(long taskId) throws ResourceNotFoundException;
}
