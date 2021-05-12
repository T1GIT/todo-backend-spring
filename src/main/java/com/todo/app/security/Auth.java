package com.todo.app.security;

import com.todo.app.data.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.*;

public class Auth extends User implements Authentication {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(this.getRole());
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

    public static Map<String, Object> toMap(Auth user) {
        return new HashMap<>() {{
            put("id", user.id);
            put("email", user.email);
            put("name", user.name);
            put("surname", user.surname);
            put("patronymic", user.patronymic);
            put("birthdate", user.birthdate);
        }};
    }

    public static Auth fromMap(Map<String, Object> map) {
        return new Auth() {{
            this.id = (long) (int) map.get("id");
            this.email = (String) map.get("email");
            this.name = (String) map.get("name");
            this.surname = (String) map.get("surname");
            this.patronymic = (String) map.get("patronymic");
            this.birthdate = new Date((int) map.get("id")) ;
        }};
    }
}
