package com.todo.app.data.model;


import com.todo.app.data.util.base.AuditModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;


@Entity
@Table(name = "categories")
public class Category extends AuditModel<Category> {

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private final Set<Task> tasks = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void addTask(Task task) {
        this.tasks.add(task);
        task.setCategory(this);
    }

    public void removeTask(Task task) {
        this.tasks.remove(task);
        task.setCategory(null);
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
