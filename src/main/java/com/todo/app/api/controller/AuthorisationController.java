package com.todo.app.api.controller;

import com.todo.app.api.util.exception.InvalidEmailException;
import com.todo.app.api.util.exception.InvalidPswException;
import com.todo.app.api.util.json.response.AuthJson;
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
    public AuthJson register(@RequestBody User user, HttpServletResponse response) {
        if (!Validator.email(user.getEmail()))
            throw new InvalidEmailException(user.getEmail());
        if (!Validator.psw(user.getPsw()))
            throw new InvalidPswException(user.getPsw());
        user = userService.register(user);
        return createTokens(user, response);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthJson login(@RequestBody User user, HttpServletResponse response) {
        if (!Validator.email(user.getEmail()))
            throw new InvalidEmailException(user.getEmail());
        if (!Validator.psw(user.getPsw()))
            throw new InvalidPswException(user.getPsw());
        user = userService.login(user);
        return createTokens(user, response);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping( "/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        refreshService.delete(RefreshProvider.extract(request));
        RefreshProvider.erase(response);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/refresh")
    public AuthJson refresh(HttpServletRequest request, HttpServletResponse response) {
        return updateTokens(request, response);
    }

    private AuthJson createTokens(User user, HttpServletResponse response) {
        Refresh refresh = refreshService.create(user.getId());
        RefreshProvider.attach(response, refresh.getValue());
        return new AuthJson(JwtProvider.getJwt(user));
    }

    private AuthJson updateTokens(HttpServletRequest request, HttpServletResponse response) {
        String refreshCookie = RefreshProvider.extract(request);
        Refresh refresh = refreshService.update(refreshCookie);
        RefreshProvider.attach(response, refresh.getValue());
        return new AuthJson(JwtProvider.getJwt(refresh.getUser()));
    }
}
