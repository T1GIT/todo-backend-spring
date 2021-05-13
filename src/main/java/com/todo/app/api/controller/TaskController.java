package com.todo.app.api.controller;

import com.todo.app.TodoApplication;
import com.todo.app.data.model.Task;
import com.todo.app.data.service.TaskService;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "Task controller",
        description = "Controller to provide operations with task models")
@RestController
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

//    @ResponseStatus(HttpStatus.OK)
//    @GetMapping(value = "/category/{categoryId}/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
//    public List<Task> getTasksByCategory(
//            @PathVariable long categoryId) {
//        return taskService.getOf(categoryId);
//    }
//
//    @ResponseStatus(HttpStatus.CREATED)
//    @PostMapping(value = "/category/{categoryId}/task", produces = MediaType.APPLICATION_JSON_VALUE)
//    public Task addTask(
//            @PathVariable long categoryId,
//            @RequestBody Task task) {
//        return taskService.add(categoryId, task);
//    }
//
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @PutMapping(value = "/task/{taskId}")
//    public Task updateTask(
//            @PathVariable long taskId,
//            @RequestBody Task task) {
//        return taskService.update(taskId, task);
//    }
//
//    @ResponseStatus(HttpStatus.OK)
//    @PatchMapping(value = "/task/{taskId}/completed", produces = MediaType.APPLICATION_JSON_VALUE)
//    public Task changeCompleted(
//            @PathVariable long taskId,
//            @RequestBody Task task) {
//        return taskService.setCompleted(taskId, task.isCompleted());
//    }
//
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @DeleteMapping(value = "/task/{taskId}")
//    public void deleteTask(
//            @PathVariable long taskId) {
//        taskService.delete(taskId);
//    }
}
