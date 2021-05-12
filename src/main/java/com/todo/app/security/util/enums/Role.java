package com.todo.app.security.util.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN, BASIC;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
