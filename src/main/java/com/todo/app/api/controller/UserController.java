package com.todo.app.api.controller;

import com.todo.app.data.repo.UserRepository;
import com.todo.app.data.service.UserService;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
}
