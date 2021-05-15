package com.todo.app.api.controller;

import com.todo.app.api.util.json.request.QueryJson;
import com.todo.app.data.model.User;
import com.todo.app.data.service.AdminService;
import com.todo.app.data.service.UserService;
import com.todo.app.security.auth.AuthContext;
import com.todo.app.security.token.JwtProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.AuthorizationScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "Administrator controller",
        description = "Controller to execute administrative functions")
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/query")
    public ResponseEntity<Object> execute(
            @RequestBody QueryJson queryJson) {
        try {
            return ResponseEntity.ok(adminService.executeSql(queryJson.getQuery()));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
        }
    }

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
