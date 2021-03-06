package com.todo.app.data.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.todo.app.data.util.base.AuditModel;
import com.todo.app.security.Validator;
import com.todo.app.security.util.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Schema(description = "Representation of the users table")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
public class User extends AuditModel<User> {

    @Schema(example = "example@mail.ru")
    @NotBlank
    @Size(min = 7, max = 255)
    @Pattern(regexp = Validator.EMAIL_PATTERN)
    @Column(unique = true, nullable = false, length = 256)
    protected String email;

    @Schema(example = "password1",
            accessMode = Schema.AccessMode.WRITE_ONLY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    @Size(min = 8, max = 1181)
    @Pattern(regexp = Validator.PSW_PATTERN)
    @Column(nullable = false, length = 1181)
    protected String psw;

    @Schema(example = "Ivan")
    @Size(max = 50)
    @Column(length = 50)
    protected String name;

    @Schema(example = "Ivanov")
    @Size(max = 50)
    @Column(length = 50)
    protected String surname;

    @Schema(example = "Ivanovich")
    @Size(max = 50)
    @Column(length = 50)
    protected String patronymic;

    @Schema(example = "2000-12-31")
    protected Date birthdate;

    @Schema(example = "ADMIN")
    @Enumerated(EnumType.STRING)
    protected Role role = Role.BASIC;

    @JsonIgnore
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final Set<Category> categories = new HashSet<>();

    @JsonIgnore
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final Set<Session> sessions = new HashSet<>();
}
