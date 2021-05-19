package com.todo.app.api.controller.impl;

import com.todo.app.api.config.SwaggerConfig;
import com.todo.app.data.model.Task;
import com.todo.app.data.service.TaskService;
import com.todo.app.security.auth.AuthContext;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


@Tag(name = "Task controller",
        description = "Controller to provide operations with task models")
@SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME)
@RequiredArgsConstructor
@RestController
@RequestMapping("/todo")
public class TaskControllerImpl {

    private final TaskService taskService;
    private final AuthContext authContext;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/category/{categoryId}/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Task> getTasksByCategory(
            @PathVariable long categoryId) {
        return taskService.getOf(authContext.getUser().getId(), categoryId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/category/{categoryId}/task", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addTask(
            @PathVariable long categoryId,
            @RequestBody Task task) throws URISyntaxException {
        taskService.add(authContext.getUser().getId(), categoryId, task);
        return ResponseEntity
                .created(new URI("task/" + task.getId()))
                .build();
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
