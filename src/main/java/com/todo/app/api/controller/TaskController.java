package com.todo.app.api.controller;

import com.todo.app.api.config.SwaggerConfig;
import com.todo.app.data.model.Task;
import com.todo.app.data.util.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Task controller",
        description = "Controller to provide operations with task models")
@SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME)
@RequestMapping("/todo")
public interface TaskController {

    @Operation(
            description = "Responses with list of tasks, contained by category with categoryId")
    @Parameter(
            name = "categoryId",
            description = "Id of the target category to get tasks")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tasks respond"),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorised access", content = @Content)
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(
            value = "/category/{categoryId}/tasks",
            produces = MediaType.APPLICATION_JSON_VALUE)
    List<Task> getTasksByCategory(@PathVariable long categoryId)
            throws ResourceNotFoundException;

    @Operation(
            description = "Creates new task of the category with given categoryId. Responses with header" +
                    " \"Location\": task/{taskId}")
    @Parameter(
            name = "categoryId",
            description = "Id of the target category to add task")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Task object",
            content = @Content(
                    examples = {
                            @ExampleObject(
                                    name = "Basic",
                                    description = "Gives only title of the task",
                                    value = """
                                            {
                                                "title": "do some thing"
                                            }"""
                            ),
                            @ExampleObject(
                                    name = "Full",
                                    description = "Gives more info about the task",
                                    value = """
                                            {
                                                "title": "do some thing",
                                                "description": "looooong description",
                                            }"""
                            )
                    }
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Task was created"),
            @ApiResponse(responseCode = "401", description = "Unauthorised access"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            value = "/category/{categoryId}/task",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> addTask(
            @PathVariable long categoryId,
            @RequestBody Task task)
            throws ResourceNotFoundException;

    @Operation(
            description = "Updates info about the task")
    @Parameter(
            name = "taskId",
            description = "Id of the target category to update")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Task object",
            content = @Content(
                    examples = {
                            @ExampleObject(
                                    name = "Basic",
                                    description = "Gives only title of the task",
                                    value = """
                                            {
                                                "title": "do some thing"
                                            }"""
                            ),
                            @ExampleObject(
                                    name = "Full",
                                    description = "Gives more info about the task",
                                    value = """
                                            {
                                                "title": "do some thing",
                                                "description": "looooong description",
                                            }"""
                            )
                    }
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Task was updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorised access"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(
            value = "/task/{taskId}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateTask(
            @PathVariable long taskId,
            @RequestBody Task task)
            throws ResourceNotFoundException;

    @Operation(
            description = "Changes completed state of the task")
    @Parameter(
            name = "taskId",
            description = "Id of the target category to update")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Task object with completed attribute",
            content = @Content(
                    examples = {
                            @ExampleObject(
                                    name = "Incomplete",
                                    description = "Sets completed to true and fills executing date",
                                    value = """
                                            {
                                                "completed": true
                                            }"""
                            ),
                            @ExampleObject(
                                    name = "Completed",
                                    description = "Sets completed to false and clears executing date",
                                    value = """
                                            {
                                                "completed": false,
                                            }"""
                            )
                    }
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Task was updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorised access"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(
            value = "/task/{taskId}/completed",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    void changeCompleted(
            @PathVariable long taskId,
            @RequestBody Task task)
            throws ResourceNotFoundException;

    @Operation(
            description = "Deletes task")
    @Parameter(
            name = "taskId",
            description = "Id of the target category to delete")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Task was deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorised access"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(
            value = "/task/{taskId}")
    void deleteTask(
            @PathVariable long taskId)
            throws ResourceNotFoundException;
}
