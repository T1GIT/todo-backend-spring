package com.todo.app.data.service;

import com.todo.app.security.util.enums.Role;

public interface AdminService {

    void changeRole(long userId, Role role);
}
