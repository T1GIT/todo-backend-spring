package com.todo.app.api.controller;

import com.todo.app.api.config.SwaggerConfig;
import com.todo.app.api.util.exception.IncorrectEmailException;
import com.todo.app.api.util.exception.IncorrectPswException;
import com.todo.app.data.model.User;
import com.todo.app.data.util.exception.EmailExistsException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


@Tag(name = "User controller",
        description = "Controller to provide operations with user models")
@SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME)
@RequestMapping("/user")
public interface UserController {

    @Operation(
            description = "Changes user's email address")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "New user's email",
            content = @Content(
                    examples = @ExampleObject(
                            name = "Email",
                            description = "JSON object with email",
                            value = """
                                    {
                                        "email": "example@mail.ru"
                                    }"""
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Email was changed"),
            @ApiResponse(responseCode = "409", description = "Email already exists", content = @Content),
            @ApiResponse(responseCode = "422", description = "Email is incorrect", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorised access", content = @Content)
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/email", produces = MediaType.APPLICATION_JSON_VALUE)
    void changeEmail(@RequestBody User user)
            throws EmailExistsException, IncorrectEmailException;

    @Operation(
            description = "Changes user's password")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "New user's password",
            content = @Content(
                    examples = @ExampleObject(
                            name = "Password",
                            description = "JSON object with password",
                            value = """
                                    {
                                        "psw": "password1"
                                    }"""
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Password was changed"),
            @ApiResponse(responseCode = "422", description = "Password is incorrect", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorised access", content = @Content)
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/psw", produces = MediaType.APPLICATION_JSON_VALUE)
    void changePsw(@RequestBody User user)
            throws IncorrectPswException;

    @Operation(
            description = "Updates additional info about the user")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "JSON object with info that must be changed",
            content = @Content(
                    examples = {
                            @ExampleObject(
                                    name = "Name and surname",
                                    description = "Specifies only name and surname, but may be patronymic also",
                                    value = """
                                            {
                                                "name": "Ivan",
                                                "surname": "Ivanovich"
                                            }"""
                            ),
                            @ExampleObject(
                                    name = "Birthdate",
                                    description = "In this mapping birthdate can be changed either",
                                    value = """
                                            {
                                                "birthdate": "2000-01-01"
                                            }"""
                            )
                    }
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Info was changed"),
            @ApiResponse(responseCode = "401", description = "Unauthorised access", content = @Content)
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping
    void updateUser(@RequestBody User user);

    @Operation(
            description = "Deletes user from the database and clears session cookies")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User was deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorised access", content = @Content)
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    void deleteUser(HttpServletResponse response);
}
