package com.todo.app.api.controller;

import com.todo.app.data.model.Task;
import com.todo.app.data.service.TaskService;
import com.todo.app.security.auth.AuthContext;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "Task controller",
        description = "Controller to provide operations with task models")
@RestController
@RequestMapping("/todo")
public class TaskController {

    private final TaskService taskService;
    private final AuthContext authContext;

    public TaskController(TaskService taskService, AuthContext authContext) {
        this.taskService = taskService;
        this.authContext = authContext;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/category/{categoryId}/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Task> getTasksByCategory(
            @PathVariable long categoryId) {
        return taskService.getOf(authContext.getUser().getId(), categoryId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/category/{categoryId}/task", produces = MediaType.APPLICATION_JSON_VALUE)
    public Task addTask(
            @PathVariable long categoryId,
            @RequestBody Task task) {
        return taskService.add(authContext.getUser().getId(), categoryId, task);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/task/{taskId}")
    public void updateTask(
            @PathVariable long taskId,
            @RequestBody Task task) {
        taskService.update(authContext.getUser().getId(), taskId, task);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/task/{taskId}/completed", produces = MediaType.APPLICATION_JSON_VALUE)
    public void changeCompleted(
            @PathVariable long taskId,
            @RequestBody Task task) {
        taskService.setCompleted(authContext.getUser().getId(), taskId, task.isCompleted());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/task/{taskId}")
    public void deleteTask(
            @PathVariable long taskId) {
        taskService.delete(authContext.getUser().getId(), taskId);
    }
}
