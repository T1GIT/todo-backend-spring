package com.todo.app.data.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.todo.app.data.util.base.AuditModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;


@Schema(description = "Representation of the tasks table")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tasks")
public class Task extends AuditModel<Task> {

    @Schema(example = "Do some things")
    @NotNull
    @Size(max = 255)
    @Column(nullable = false)
    protected String title;

    @Schema(example = "Long describing, what I wanted to do")
    @Size(max = 1000)
    @Column(columnDefinition = "text", length = 1000)
    protected String description;

    @Schema(example = "true")
    @NotNull
    @Column(nullable = false)
    protected boolean completed = false;

    @Schema(example = "2020-12-30")
    private Date executeDate;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Category category;

}
