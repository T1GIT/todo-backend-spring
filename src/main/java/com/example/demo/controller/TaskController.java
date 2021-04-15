package com.example.demo.controller;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Category;
import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.service.CategoryService;
import com.example.demo.service.TaskService;
import com.example.demo.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;
    private final CategoryService categoryService;
    
    public TaskController(TaskService taskService, UserService userService, CategoryService categoryService) {
        this.taskService = taskService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @GetMapping("/tasks")
    public Page<Task> getTasks(Pageable pageable) {
        return taskService.getAll(pageable);
    }

    @GetMapping("/tasks/{taskId}")
    public Task getTaskById(@PathVariable long taskId) {
        return taskService.getById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskId));
    }

    @GetMapping("/users/{userId}/tasks")
    public List<Task> getTasksByUser(@PathVariable Long userId) {
        User user = userService.getById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
        return taskService.getByUser(user);
    }

    @GetMapping("/users/{userId}/category/{categoryId}/tasks")
    public List<Task> getTasksByCategory(@PathVariable Long userId, @PathVariable Long categoryId) {
        Category category = categoryService.getById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + categoryId));
        User user = userService.getById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
        return taskService.getByUserAndCategory(user, category);
    }

    @PostMapping("/users/{userId}/tasks")
    public Task createTask(
            @PathVariable long userId,
            @Valid @RequestBody Task task) {
        return userService.getById(userId)
                .map(user -> {
                    user.addTask(task);
                    return task;
                }).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }

    @PutMapping("/tasks/{taskId}/{categoryId}")
    public Task addCategory(
            @PathVariable long taskId,
            @PathVariable long categoryId) {
        Task task = taskService.getById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskId));
        Category category = categoryService.getById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + categoryId));
        task.addCategory(category);
        taskService.update(task);
        return task;
    }

    @PutMapping("/users/{userId}/tasks/{taskId}")
    public Task updateTask(
            @PathVariable long userId,
            @PathVariable long taskId,
            @Valid @RequestBody Task taskRequest) {
        return userService.getById(userId)
                .map(user -> taskService.getById(taskId)
                        .map(task -> {
                            task.setName(taskRequest.getName());
                            task.setDescription(taskRequest.getDescription());
                            task.setExecuteDate(taskRequest.getExecuteDate());
                            task.setCompleted(taskRequest.isCompleted());
                            task.setUser(user);
                            return task;
                        }).orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskId)))
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }

    @DeleteMapping("/users/{userId}/tasks/{taskId}")
    public ResponseEntity<?> deleteTask(
            @PathVariable long userId,
            @PathVariable long taskId,
            @Valid @RequestBody Task taskRequest) {
        return userService.getById(userId)
                .map(user -> taskService.getById(taskId)
                        .map(task -> {
                            user.removeTask(task);
                            return ResponseEntity.ok().build();
                        }).orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskId)))
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }
}
