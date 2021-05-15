package com.todo.app.api.controller;

import com.todo.app.data.model.User;
import com.todo.app.data.service.AdminService;
import com.todo.app.security.token.JwtProvider;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Api(tags = "Administrator controller",
        description = "Controller to execute administrative functions")
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/jwt/update/key")
    public void updateJwtKey() {
        JwtProvider.updateKey();
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/user/{userId}/role")
    public User setRole(
            @PathVariable long userId,
            @RequestBody User user) {
        return adminService.changeRole(userId, user.getRole());
    }
}
