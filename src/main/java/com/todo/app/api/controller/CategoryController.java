package com.todo.app.api.controller;

import com.todo.app.api.config.SwaggerConfig;
import com.todo.app.data.model.Category;
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


@Tag(name = "Category controller",
        description = "Controller to provide operations with category models")
@SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME)
@RequestMapping("/todo/categories")
public interface CategoryController {

    @Operation(
            description = "Responses categories list of the authorised user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User was login"),
            @ApiResponse(responseCode = "401", description = "Unauthorised access", content = @Content)
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    List<Category> getCategoriesByUser();

    @Operation(
            description = "Creates new category with the authorised user. Responses with header" +
                    " \"Location\": category/{categoryId}")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Category object",
            content = @Content(
                    examples = @ExampleObject(
                            name = "Json category",
                            description = "Gives only name of the category",
                            value = """
                                      {
                                      "name": "Shopping list"
                                    }"""
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Category was created"),
            @ApiResponse(responseCode = "401", description = "Unauthorised access", content = @Content)
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> addCategory(
            @RequestBody Category category);

    @Operation(
            description = "Change category's name")
    @Parameter(
            name = "categoryId",
            description = "Id of the target category for changing name")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Category object with name",
            content = @Content(
                    examples = @ExampleObject(
                            name = "Json category",
                            description = "Gives only name of the category",
                            value = """
                                      {
                                      "name": "Some another name"
                                    }"""
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Category was created"),
            @ApiResponse(responseCode = "401", description = "Unauthorised access"),
            @ApiResponse(responseCode = "403", description = "Authorised user is not an owner of the category")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{categoryId}/name", produces = MediaType.APPLICATION_JSON_VALUE)
    void changeName(
            @PathVariable long categoryId,
            @RequestBody Category category);

    @Operation(
            description = "Deletes category and all its tasks")
    @Parameter(
            name = "categoryId",
            description = "Id of the target category to delete")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Category was deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorised access"),
            @ApiResponse(responseCode = "403", description = "Authorised user is not an owner of the category")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{categoryId}")
    void deleteCategory(
            @PathVariable long categoryId);
}
