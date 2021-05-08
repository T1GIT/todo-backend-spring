package com.todo.app.service;


import com.todo.app.model.Category;
import com.todo.app.model.Task;
import com.todo.app.model.User;
import com.todo.app.utils.abstractService.ServiceInterface;

import java.util.List;

public interface TaskService extends ServiceInterface<Task> {
    List<Task> getByUserAndCategory(User user, Category category);

    List<Task> getByUser(User user);
}
