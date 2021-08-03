package com.todo.app.api.controller.impl;

import com.todo.app.data.model.Task;
import com.todo.app.data.service.TaskService;
import com.todo.app.security.auth.AuthContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;


@RequiredArgsConstructor
@RestController
public class TaskControllerImpl implements com.todo.app.api.controller.TaskController {

    private final TaskService taskService;
    private final AuthContext authContext;

    @Override
    public List<Task> getTasksByCategory(long categoryId) {
        return taskService.getOf(authContext.getUser().getId(), categoryId);
    }

    @Override
    public ResponseEntity<Void> addTask(long categoryId, Task task) {
        taskService.add(authContext.getUser().getId(), categoryId, task);
        return ResponseEntity
                .created(URI.create("categories/" + categoryId + "/tasks/" + task.getId()))
                .build();
    }

    @Override
    public void updateTask(long categoryId, long taskId, Task task) {
        taskService.update(authContext.getUser().getId(), categoryId, taskId, task);
    }

    @Override
    public void changeCompleted(long categoryId, long taskId, Task task) {
        taskService.setCompleted(authContext.getUser().getId(), categoryId, taskId, task.isCompleted());
    }

    @Override
    public void deleteTask(long categoryId, long taskId) {
        taskService.delete(authContext.getUser().getId(), categoryId, taskId);
    }
}
