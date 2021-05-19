package com.todo.app.api.controller.impl;

import com.todo.app.api.util.exception.IncorrectEmailException;
import com.todo.app.api.util.exception.IncorrectPswException;
import com.todo.app.data.model.User;
import com.todo.app.data.service.UserService;
import com.todo.app.security.Validator;
import com.todo.app.security.auth.AuthContext;
import com.todo.app.security.token.RefreshProvider;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


@Tag(name = "User controller",
        description = "Controller to provide operations with user models")
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserControllerImpl {

    private final UserService userService;
    private final AuthContext authContext;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/email", produces = MediaType.APPLICATION_JSON_VALUE)
    public void changeEmail(
            @Parameter(description = "DESC")
            @RequestBody User user) {
        if (!Validator.email(user.getEmail()))
            throw new IncorrectEmailException(user.getEmail());
        userService.changeEmail(authContext.getUser().getId(), user.getEmail());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/psw", produces = MediaType.APPLICATION_JSON_VALUE)
    public void changePsw(
            @RequestBody User user) {
        if (!Validator.psw(user.getPsw()))
            throw new IncorrectPswException(user.getPsw());
        userService.changePsw(authContext.getUser().getId(), user.getPsw());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping()
    public void updateUser(
            @RequestBody User user) {
        userService.update(authContext.getUser().getId(), user);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping()
    public void deleteUser(HttpServletResponse response) {
        userService.delete(authContext.getUser().getId());
        RefreshProvider.erase(response);
    }
}
