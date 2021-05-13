package com.todo.app.api.controller;

import com.todo.app.api.controller.util.exception.InvalidEmailException;
import com.todo.app.api.controller.util.exception.InvalidPswException;
import com.todo.app.data.model.Refresh;
import com.todo.app.data.model.User;
import com.todo.app.data.service.RefreshService;
import com.todo.app.data.service.UserService;
import com.todo.app.security.Validator;
import com.todo.app.security.token.JwtProvider;
import com.todo.app.security.token.RefreshProvider;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(tags = "Authorisation controller",
        description = "Controller to provide authorisation process")
@RestController
@RequestMapping("/authorisation")
public class AuthorisationController {

    private final UserService userService;
    private final RefreshService refreshService;

    public AuthorisationController(UserService userService, RefreshService refreshService) {
        this.userService = userService;
        this.refreshService = refreshService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public User register(@RequestBody User user, HttpServletResponse response) {
        if (!Validator.email(user.getEmail()))
            throw new InvalidEmailException(user.getEmail());
        if (!Validator.psw(user.getPsw()))
            throw new InvalidPswException(user.getPsw());
        user = userService.register(user);
        Refresh refresh = refreshService.create(user.getId());
        JwtProvider.attach(response, user);
        RefreshProvider.attach(response, refresh.getValue());
        return user;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public User login(@RequestBody User user, HttpServletResponse response) {
        if (!Validator.email(user.getEmail()))
            throw new InvalidEmailException(user.getEmail());
        if (!Validator.psw(user.getPsw()))
            throw new InvalidPswException(user.getPsw());
        user = userService.login(user);
        Refresh refresh = refreshService.create(user.getId());
        JwtProvider.attach(response, user);
        RefreshProvider.attach(response, refresh.getValue());
        return user;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(value = "/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshCookie = RefreshProvider.extract(request);
        refreshService.delete(refreshCookie);
        JwtProvider.erase(response);
        RefreshProvider.erase(response);
    }
}
