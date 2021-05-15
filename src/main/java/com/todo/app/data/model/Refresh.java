package com.todo.app.data.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.todo.app.data.util.base.AuditModel;
import io.swagger.annotations.ApiModelProperty;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@ApiIgnore
@Entity
@Table(name = "tokens")
public class Refresh extends AuditModel<Refresh> {

    @ApiModelProperty(position = 0, example = "fihefUHFUe7EFhuh8987HOfheuhEUg38Hefhus802efF")
    @NotNull
    @Column(nullable = false, unique = true)
    private String value;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
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
