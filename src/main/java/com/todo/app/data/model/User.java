package com.todo.app.data.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.todo.app.data.util.base.AuditModel;
import com.todo.app.security.Validator;
import com.todo.app.security.util.enums.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@ApiModel
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
@JsonIgnoreProperties(value = "psw", allowSetters = true)
public class User extends AuditModel<User> {

    @ApiModelProperty(position = 0, example = "example@mail.ru")
    @NotBlank
    @Size(min = 7, max = 255)
    @Pattern(regexp = Validator.EMAIL_PATTERN)
    @Column(unique = true, nullable = false, length = 256)
    protected String email;

    @ApiModelProperty(position = 1, example = "password1")
    @NotBlank
    @Size(min = 8, max = 1181)
    @Pattern(regexp = Validator.PSW_PATTERN)
    @Column(nullable = false, length = 1181)
    protected String psw;

    @ApiModelProperty(position = 2, example = "Ivan")
    @Size(max = 50)
    @Column(length = 50)
    protected String name;

    @ApiModelProperty(position = 3, example = "Ivanov")
    @Size(max = 50)
    @Column(length = 50)
    protected String surname;

    @ApiModelProperty(position = 4, example = "Ivanovich")
    @Size(max = 50)
    @Column(length = 50)
    protected String patronymic;

    @ApiModelProperty(position = 5, example = "2020-12-31")
    protected Date birthdate;

    @ApiModelProperty(position = 6, example = "ADMIN")
    @Enumerated(EnumType.STRING)
    protected Role role = Role.BASIC;

    @JsonIgnore
    @Setter(AccessLevel.NONE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Category> categories = new HashSet<>();
    
    @JsonIgnore
    @Setter(AccessLevel.NONE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Session> sessions = new HashSet<>();

    public void addCategory(Category category) {
        this.categories.add(category);
        category.setUser(this);
    }

    public void removeCategory(Category category) {
        this.categories.remove(category);
        category.setUser(null);
    }

    public void addSession(Session session) {
        this.sessions.add(session);
        session.setUser(this);
    }

    public void removeSession(Session session) {
        this.sessions.remove(session);
        session.setUser(null);
    }
}
