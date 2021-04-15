package com.example.demo.controller;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public Page<User> getUsers(Pageable pageable) {
        return userService.getAll(pageable);
    }

    @GetMapping("/users/{userId}")
    public User getUserById(@PathVariable long userId) {
        return userService.getById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }

    @GetMapping("/users/{login}")
    public User getUserByLogin(@PathVariable String login) {
        return userService.getByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with login " + login));
    }

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) {
        return userService.add(user);
    }

    @PutMapping("/users/{userId}")
    public User updateUser(@PathVariable long userId, @Valid @RequestBody User userRequest) {
        return userService.getById(userId)
                .map(user -> {
                    user.setName(userRequest.getName());
                    return userService.add(user);
                }).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable long userId) {
        return userService.getById(userId)
                .map(user -> {
                    userService.delete(user);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }
}
