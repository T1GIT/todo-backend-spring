package com.todo.app.security.auth.impl;

import com.todo.app.data.model.User;
import com.todo.app.security.auth.AuthContext;
import com.todo.app.security.util.exception.AbsentAuthorisationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthContextImpl implements AuthContext {
    @Override
    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated())
            throw new AbsentAuthorisationException();
        return (User) authentication;
    }
}
