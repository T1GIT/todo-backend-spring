package com.todo.app.api.controller;

import com.todo.app.api.util.exception.InvalidEmailException;
import com.todo.app.api.util.exception.InvalidPswException;
import com.todo.app.data.model.User;
import com.todo.app.data.service.UserService;
import com.todo.app.security.auth.AuthContext;
import com.todo.app.security.Validator;
import com.todo.app.security.token.RefreshProvider;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


@Api(tags = "User controller",
        description = "Provides operations with user models")
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AuthContext authContext;

    public UserController(UserService userService, AuthContext authContext) {
        this.userService = userService;
        this.authContext = authContext;
    }

    @ApiOperation("Validates and changes user email address")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/email", produces = MediaType.APPLICATION_JSON_VALUE)
    public void changeEmail(
            @ApiParam(value = "DESC")
            @RequestBody User user) {
        if (!Validator.email(user.getEmail()))
            throw new InvalidEmailException(user.getEmail());
        userService.changeEmail(authContext.getUser().getId(), user.getEmail());
    }

    @ApiOperation("Validates and changes user's password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/psw", produces = MediaType.APPLICATION_JSON_VALUE)
    public void changePsw(
            @RequestBody User user) {
        if (!Validator.psw(user.getPsw()))
            throw new InvalidPswException(user.getPsw());
        userService.changePsw(authContext.getUser().getId(), user.getPsw());
    }

    @ApiOperation("Updates information about the user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping()
    public void updateUser(
            @RequestBody User user) {
        userService.update(authContext.getUser().getId(), user);
    }

    @ApiOperation("Deletes the user")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Entity was deleted or not existed")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping()
    public void deleteUser(HttpServletResponse response) {
        userService.delete(authContext.getUser().getId());
        RefreshProvider.erase(response);
    }
}
