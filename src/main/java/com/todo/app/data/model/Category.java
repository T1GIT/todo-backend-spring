package com.todo.app.data.model;


import com.todo.app.data.util.base.AuditModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.util.*;


@ApiModel
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "categories")
public class Category extends AuditModel<Category> {

    @ApiModelProperty(position = 0, example = "Shopping list")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String name;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User user;

    @JsonIgnore
    @Setter(AccessLevel.NONE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Task> tasks = new HashSet<>();

    public void addTask(Task task) {
        this.tasks.add(task);
        task.setCategory(this);
    }

    public void removeTask(Task task) {
        this.tasks.remove(task);
        task.setCategory(null);
    }
}
