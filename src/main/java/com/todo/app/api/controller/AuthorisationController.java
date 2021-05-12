package com.todo.app.api.controller;

import com.todo.app.api.controller.util.exception.InvalidEmailException;
import com.todo.app.api.controller.util.exception.InvalidPswException;
import com.todo.app.data.model.User;
import com.todo.app.data.service.UserService;
import com.todo.app.security.Validator;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Authorisation controller",
        description = "Controller to provide authorisation process")
@RestController
public class AuthorisationController {

    private final UserService userService;

    public AuthorisationController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/authorisation/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public User register(@RequestBody User user) {
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        System.out.println(SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
        if (!Validator.email(user.getEmail()))
            throw new InvalidEmailException(user.getEmail());
        if (!Validator.psw(user.getPsw()))
            throw new InvalidPswException(user.getPsw());
        return userService.register(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/authorisation/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public User login(@RequestBody User user) {
        if (!Validator.email(user.getEmail()))
            throw new InvalidEmailException(user.getEmail());
        if (!Validator.psw(user.getPsw()))
            throw new InvalidPswException(user.getPsw());
        return userService.login(user);
    }
}
