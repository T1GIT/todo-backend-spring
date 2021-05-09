package com.todo.app.data.service;


import com.todo.app.data.exception.ResourceNotFoundException;
import com.todo.app.data.model.Task;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;


public interface TaskService {

    List<Task> getOf(long categoryId) throws ResourceNotFoundException;

    Task add(long categoryId, Task task) throws ResourceNotFoundException;

    Task update(long taskId, Consumer<Task> editor) throws ResourceNotFoundException;

    Task changeCategory(long taskId, long newCategoryId) throws ResourceNotFoundException;

    void delete(long taskId) throws ResourceNotFoundException;
}
