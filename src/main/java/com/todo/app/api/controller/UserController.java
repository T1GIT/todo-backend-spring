package com.todo.app.api.controller;

import com.todo.app.TodoApplication;
import com.todo.app.api.controller.util.exception.InvalidEmailException;
import com.todo.app.api.controller.util.exception.InvalidPswException;
import com.todo.app.data.model.User;
import com.todo.app.data.service.UserService;
import com.todo.app.security.Validator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@Api(tags = "User controller",
        description = "Controller to provide operations with user models")
@RestController
@RequestMapping(TodoApplication.API_ROOT)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/user/{userId}/email", produces = MediaType.APPLICATION_JSON_VALUE)
    public User changeEmail(
            @PathVariable long userId,
            @RequestBody User user) {
        if (!Validator.email(user.getEmail()))
            throw new InvalidEmailException(user.getEmail());
        return userService.changeEmail(userId, user.getEmail());
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/user/{userId}/psw", produces = MediaType.APPLICATION_JSON_VALUE)
    public User changePsw(
            @PathVariable long userId,
            @RequestBody User user) {
        if (!Validator.psw(user.getPsw()))
            throw new InvalidPswException(user.getPsw());
        return userService.changePsw(userId, user.getPsw());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/user/{userId}")
    public void updateUser(
            @PathVariable long userId,
            @RequestBody User user) {
        userService.update(userId, user);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/user/{userId}")
    public void deleteUser(
            @PathVariable long userId) {
        userService.delete(userId);
    }
}
