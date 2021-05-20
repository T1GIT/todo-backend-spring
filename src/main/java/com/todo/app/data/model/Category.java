package com.todo.app.data.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.todo.app.data.util.base.AuditModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;


@Schema(description = "Representation of the categories table")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "categories")
public class Category extends AuditModel<Category> {

    @Schema(example = "Shopping list")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String name;

    @JsonIgnore
    @Setter(AccessLevel.NONE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Task> tasks = new HashSet<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User user;

    public void addTask(Task task) {
        this.tasks.add(task);
        task.setCategory(this);
    }

    public void removeTask(Task task) {
        this.tasks.remove(task);
        task.setCategory(null);
    }
}
