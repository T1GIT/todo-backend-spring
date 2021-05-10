package com.todo.app.api.controller.util.json;

import com.todo.app.data.model.User;

import java.io.Serializable;

public class AuthResponse implements Serializable {
    private String jwt;
    private String refresh;
    private User user;

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
