package com.todo.app.security.auth;

import com.todo.app.security.util.enums.Role;
import org.springframework.security.core.GrantedAuthority;

public class RoleAuthority implements GrantedAuthority {

    private final String role;

    public RoleAuthority(Role role) {
        this.role = "ROLE_" + role.name();
    }

    @Override
    public String getAuthority() {
        return role;
    }
}
