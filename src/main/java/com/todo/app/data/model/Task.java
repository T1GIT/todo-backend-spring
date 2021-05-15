package com.todo.app.data.model;


import com.todo.app.data.util.base.AuditModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;


@ApiModel
@Entity
@Table(name = "tasks")
public class Task extends AuditModel<Task> {

    @ApiModelProperty(position = 0, example = "Do some things")
    @NotNull
    @Size(max = 255)
    @Column(nullable = false)
    private String title;

    @ApiModelProperty(position = 1, example = "Long describing, what I wanted to do")
    @Size(max = 1000)
    @Column(columnDefinition = "text", length = 1000)
    private String description;

    @ApiModelProperty(position = 2, example = "true")
    @NotNull
    @Column(nullable = false)
    private boolean completed = false;

    @ApiModelProperty(position = 3, example = "2020-12-30")
    private Date executeDate;

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
