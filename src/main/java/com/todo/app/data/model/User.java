package com.todo.app.data.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.todo.app.data.util.base.AuditModel;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.*;


@Entity
@Table(name = "users")
public class User extends AuditModel<User> {

    @Size(min = 5, max = 256)
    @Column(unique = true, nullable = false, length = 256)
    private String email;

    @Column(nullable = false, length = 1180)
    private String psw;

    private String name;

    private String surname;

    private String patronymic;

    private Date birthdate;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Category> categories = new HashSet<>();

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

    public void addCategory(Category category) {
        this.categories.add(category);
        category.setUser(this);
    }

    public void removeCategory(Category category) {
        this.categories.remove(category);
        category.setUser(null);
    }

    @Override
    public String toString() {
        return "User{" +
                "mail='" + email + '\'' +
                ", psw='" + psw + '\'' +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", birthdate=" + birthdate +
                ", id=" + id +
                '}';
    }
}
