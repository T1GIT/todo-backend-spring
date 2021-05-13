package com.todo.app.security.auth;

import com.todo.app.data.model.User;
import com.todo.app.security.util.enums.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

public class AuthUser extends User implements Authentication {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new RoleAuthority(this.getRole()));
    }

    @Override
    public User getCredentials() {
        return this;
    }

    @Override
    public User getDetails() {
        return this;
    }

    @Override
    public User getPrincipal() {
        return this;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return super.getName();
    }

    public static Map<String, Object> toMap(User user) {
        return new HashMap<>() {{
            put("id", user.getId());
            put("email", user.getEmail());
            put("name", user.getName());
            put("surname", user.getSurname());
            put("patronymic", user.getPatronymic());
            put("birthdate", user.getBirthdate());
            put("role", user.getRole());
        }};
    }

    public static AuthUser fromMap(Map<String, Object> map) {
        return new AuthUser() {{
            this.id = (long) (int) map.get("id");
            this.email = (String) map.get("email");
            this.name = (String) map.get("name");
            this.surname = (String) map.get("surname");
            this.patronymic = (String) map.get("patronymic");
            this.birthdate = new Date((int) map.get("id"));
            this.role = Role.valueOf((String) map.get("role"));
        }};
    }
}
