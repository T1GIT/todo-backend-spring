package com.todo.app.api.controller;

import com.todo.app.TodoApplication;
import com.todo.app.api.controller.util.json.AuthResponse;
import com.todo.app.data.model.User;
import com.todo.app.data.service.UserService;
import com.todo.app.security.Validator;
import com.todo.app.api.controller.util.exception.InvalidEmailException;
import com.todo.app.api.controller.util.exception.InvalidPswException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(TodoApplication.API_ROOT + "/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody User user) {
        if (!Validator.email(user.getEmail()))
            throw new InvalidEmailException(user.getEmail());
        if (!Validator.psw(user.getPsw()))
            throw new InvalidPswException(user.getPsw());
        user = userService.register(user);
        AuthResponse response = new AuthResponse();
        response.setUser(user);
        response.setJwt("jwt");
        response.setRefresh("refresh"); // TODO: insert jwt
        return response;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody User user) {
        if (!Validator.email(user.getEmail()))
            throw new InvalidEmailException(user.getEmail());
        if (!Validator.psw(user.getPsw()))
            throw new InvalidPswException(user.getPsw());
        user = userService.login(user);
        AuthResponse response = new AuthResponse();
        response.setUser(user);
        response.setJwt("jwt");
        response.setRefresh("refresh"); // TODO: insert jwt
        return response;
    }

    @PatchMapping("{userId}/email")
    public ResponseEntity<Object> changeEmail(
            @PathVariable long userId,
            @RequestBody User user) {
        if (!Validator.email(user.getEmail()))
            throw new InvalidEmailException(user.getEmail());
        user = userService.changeEmail(userId, user.getEmail());
        return ResponseEntity.ok(user);
    }

    @PatchMapping("{userId}/psw")
    public ResponseEntity<Object> changePsw(
            @PathVariable long userId,
            @RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        if (!Validator.psw(user.getPsw()))
            throw new InvalidPswException(user.getPsw());
        user = userService.changePsw(userId, user.getPsw());
        return ResponseEntity.ok(user);
    }

    @PutMapping("{userId}")
    public ResponseEntity<Object> update(
            @PathVariable long userId,
            @RequestBody User user) {
        user = userService.update(userId, user);
        return ResponseEntity.ok(user);
    }
}
