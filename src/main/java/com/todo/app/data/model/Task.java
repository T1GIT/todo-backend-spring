package com.todo.app.data.model;


import com.todo.app.data.util.base.AuditModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;


@Entity
@Table(name = "tasks")
public class Task extends AuditModel<Task> {

    @NotNull
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String title;

    @Size(max = 1000)
    @Column(columnDefinition = "text", length = 1000)
    private String description;

    private Date executeDate;

    @NotNull
    @Column(nullable = false)
    private boolean completed = false;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Category category;

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getExecuteDate() {
        return executeDate;
    }

    public void setExecuteDate(Date executeDate) {
        this.executeDate = executeDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", executeDate=" + executeDate +
                ", completed=" + completed +
                ", id=" + id +
                '}';
    }
}
