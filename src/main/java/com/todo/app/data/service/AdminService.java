package com.todo.app.data.service;

import com.todo.app.data.model.User;
import com.todo.app.security.util.enums.Role;

import java.util.List;

public interface AdminService {

    User changeRole(long userId, Role role);
}
