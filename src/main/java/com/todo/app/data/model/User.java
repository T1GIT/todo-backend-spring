package com.todo.app.data.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.todo.app.data.util.base.AuditModel;
import com.todo.app.security.util.enums.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiResponse;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;


@ApiModel
@Entity
@Table(name = "users")
@JsonIgnoreProperties(value = "psw", allowSetters = true)
public class User extends AuditModel<User> {

    @ApiModelProperty(position = 0, example = "example@mail.ru")
    @NotNull
    @Size(min = 7, max = 255)
    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,63})$")
    @Column(unique = true, nullable = false, length = 256)
    protected String email;

    @ApiModelProperty(position = 1, example = "password1")
    @NotNull
    @Size(min = 8, max = 1181)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Zа-яА-Я]).*$")
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

    @ApiModelProperty(accessMode = ApiModelProperty.AccessMode.READ_ONLY, position = 6)
    @Enumerated(EnumType.STRING)
    protected Role role = Role.BASIC;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Category> categories = new HashSet<>();
    
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Refresh> refreshes = new HashSet<>();

    public String getEmail() {
        return email;
    }

    public void setEmail(String login) {
        this.email = login;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }
    
    public Set<Refresh> getRefreshes() {
        return refreshes;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void addCategory(Category category) {
        this.categories.add(category);
        category.setUser(this);
    }

    public void removeCategory(Category category) {
        this.categories.remove(category);
        category.setUser(null);
    }

    public void addRefresh(Refresh refresh) {
        this.refreshes.add(refresh);
        refresh.setUser(this);
    }

    public void removeRefresh(Refresh refresh) {
        this.refreshes.remove(refresh);
        refresh.setUser(null);
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", psw='" + psw + '\'' +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", birthdate=" + birthdate +
                ", id=" + id +
                '}';
    }
}
