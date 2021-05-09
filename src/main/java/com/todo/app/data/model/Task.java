package com.todo.app.data.model;


import com.todo.app.data.util.base.AuditModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "tasks")
public class Task extends AuditModel<Task> {

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    private Date executeDate;

    private boolean completed;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnore
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
