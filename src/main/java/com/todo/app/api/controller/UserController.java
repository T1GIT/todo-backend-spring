package com.todo.app.api.controller;

import com.todo.app.TodoApplication;
import com.todo.app.api.controller.util.exception.InvalidEmailException;
import com.todo.app.api.controller.util.exception.InvalidPswException;
import com.todo.app.data.model.User;
import com.todo.app.data.service.UserService;
import com.todo.app.security.Auth;
import com.todo.app.security.Validator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@Api(tags = "User controller",
        description = "Controller to provide operations with user models")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/user/email", produces = MediaType.APPLICATION_JSON_VALUE)
    public User changeEmail(
            @RequestBody User requestUser) {
        if (!Validator.email(requestUser.getEmail()))
            throw new InvalidEmailException(requestUser.getEmail());
        Auth authUser = (Auth) SecurityContextHolder.getContext().getAuthentication();
        return userService.changeEmail(authUser.getId(), requestUser.getEmail());
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/user/psw", produces = MediaType.APPLICATION_JSON_VALUE)
    public User changePsw(
            @RequestBody User requestUser) {
        if (!Validator.psw(requestUser.getPsw()))
            throw new InvalidPswException(requestUser.getPsw());
        Auth authUser = (Auth) SecurityContextHolder.getContext().getAuthentication();
        return userService.changePsw(authUser.getId(), requestUser.getPsw());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/user")
    public void updateUser(
            @RequestBody User requestUser) {
        Auth authUser = (Auth) SecurityContextHolder.getContext().getAuthentication();
        userService.update(authUser.getId(), requestUser);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/user")
    public void deleteUser() {
        Auth authUser = (Auth) SecurityContextHolder.getContext().getAuthentication();
        userService.delete(authUser.getId());
    }
}
