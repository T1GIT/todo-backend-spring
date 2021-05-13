package com.todo.app.api.controller;

import com.todo.app.api.controller.util.exception.InvalidEmailException;
import com.todo.app.api.controller.util.exception.InvalidPswException;
import com.todo.app.data.model.User;
import com.todo.app.data.service.UserService;
import com.todo.app.security.auth.AuthContext;
import com.todo.app.security.auth.AuthUser;
import com.todo.app.security.Validator;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@Api(tags = "User controller",
        description = "Controller to provide operations with user models")
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AuthContext authContext;

    public UserController(UserService userService, AuthContext authContext) {
        this.userService = userService;
        this.authContext = authContext;
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/email", produces = MediaType.APPLICATION_JSON_VALUE)
    public User changeEmail(
            @RequestBody User user) {
        if (!Validator.email(user.getEmail()))
            throw new InvalidEmailException(user.getEmail());
        return userService.changeEmail(authContext.getUser().getId(), user.getEmail());
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/psw", produces = MediaType.APPLICATION_JSON_VALUE)
    public User changePsw(
            @RequestBody User user) {
        if (!Validator.psw(user.getPsw()))
            throw new InvalidPswException(user.getPsw());
        return userService.changePsw(authContext.getUser().getId(), user.getPsw());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping()
    public void updateUser(
            @RequestBody User user) {
        userService.update(authContext.getUser().getId(), user);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping()
    public void deleteUser() {
        userService.delete(authContext.getUser().getId());
    }
}
