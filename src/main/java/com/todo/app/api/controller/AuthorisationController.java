package com.todo.app.api.controller;

import com.todo.app.TodoApplication;
import com.todo.app.api.controller.util.exception.InvalidEmailException;
import com.todo.app.api.controller.util.exception.InvalidPswException;
import com.todo.app.api.controller.util.json.AuthResponse;
import com.todo.app.data.model.User;
import com.todo.app.data.service.UserService;
import com.todo.app.security.Validator;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(TodoApplication.API_ROOT + "/authorisation")
public class AuthorisationController {

    private final UserService userService;

    public AuthorisationController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthResponse register(@RequestBody User user) {
        if (!Validator.email(user.getEmail()))
            throw new InvalidEmailException(user.getEmail());
        if (!Validator.psw(user.getPsw()))
            throw new InvalidPswException(user.getPsw());
        user = userService.register(user);
        AuthResponse response = new AuthResponse();
        response.setUser(user);
        return response;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthResponse login(@RequestBody User user) {
        if (!Validator.email(user.getEmail()))
            throw new InvalidEmailException(user.getEmail());
        if (!Validator.psw(user.getPsw()))
            throw new InvalidPswException(user.getPsw());
        user = userService.login(user);
        AuthResponse response = new AuthResponse();
        response.setUser(user);
        return response;
    }
}
