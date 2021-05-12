package com.todo.app.data.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.todo.app.data.util.base.AuditModel;
import com.todo.app.security.util.enums.SecretLength;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Object-oriented representation for table <u> tokens </u>
 * <p>
 * <b>Storages:</b>
 * Refresh tokens of users
 * <p>
 * <b>Logic:</b>
 * If JWT token doesn't exist or invalid then if request contains
 * {@link Refresh refresh token} {@link User user}  can be identified by that and receive fresh JWT
 * with updating {@link Refresh refresh token}.
 */
@Entity
@Table(name = "tokens")
public class Refresh extends AuditModel<Refresh> {

    @NotNull
    @Column(nullable = false, unique = true)
    private String value;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "users_id", nullable = false, updatable = false)
    private User user;

    public String getValue() {
        return value;
    }

    public void setValue(String token) {
        this.value = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Token{" +
                "value='" + value +
                '}';
    }
}
