package com.todo.app.security.auth;

import com.todo.app.data.model.User;

public interface AuthContext {
    User getUser();
}
