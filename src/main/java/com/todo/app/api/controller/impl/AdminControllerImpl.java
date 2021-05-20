package com.todo.app.api.controller.impl;

import com.todo.app.api.controller.AdminController;
import com.todo.app.data.model.User;
import com.todo.app.data.service.AdminService;
import com.todo.app.security.token.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class AdminControllerImpl implements AdminController {

    private final AdminService adminService;

    @Override
    public void updateJwtKey() {
        JwtProvider.updateKey();
    }

    @Override
    public void changeRole(long userId, User user) {
        adminService.changeRole(userId, user.getRole());
    }
}
