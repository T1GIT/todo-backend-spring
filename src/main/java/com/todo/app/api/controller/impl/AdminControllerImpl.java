package com.todo.app.api.controller.impl;

import com.todo.app.data.model.User;
import com.todo.app.data.service.AdminService;
import com.todo.app.security.token.JwtProvider;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Administrator controller",
        description = "Controller to execute administrative functions")
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminControllerImpl {

    private final AdminService adminService;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/update-jwt-key")
    public void updateJwtKey() {
        JwtProvider.updateKey();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/user/{userId}/role")
    public void changeRole(
            @PathVariable long userId,
            @RequestBody User user) {
        adminService.changeRole(userId, user.getRole());
    }
}
