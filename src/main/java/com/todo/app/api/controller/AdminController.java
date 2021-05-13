package com.todo.app.api.controller;

import com.todo.app.api.controller.util.json.QueryJson;
import com.todo.app.data.service.AdminService;
import com.todo.app.data.service.UserService;
import com.todo.app.security.auth.AuthContext;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.util.List;


@Api(tags = "Administrator controller",
        description = "Controller to execute administrative functions")
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final AuthContext authContext;
    private final AdminService adminService;

    public AdminController(UserService userService, AuthContext authContext, AdminService adminService) {
        this.userService = userService;
        this.authContext = authContext;
        this.adminService = adminService;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/query")
    public List<Object> execute(@RequestBody QueryJson queryJson) {
        return adminService.executeSql(queryJson.getQuery());
    }

}
