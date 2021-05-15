package com.todo.app.data.model;


import com.todo.app.data.util.base.AuditModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;


@ApiModel
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tasks")
public class Task extends AuditModel<Task> {

    @ApiModelProperty(position = 0, example = "Do some things")
    @NotNull
    @Size(max = 255)
    @Column(nullable = false)
    protected String title;

    @ApiModelProperty(position = 1, example = "Long describing, what I wanted to do")
    @Size(max = 1000)
    @Column(columnDefinition = "text", length = 1000)
    protected String description;

    @ApiModelProperty(position = 2, example = "true")
    @NotNull
    @Column(nullable = false)
    protected boolean completed = false;

    @ApiModelProperty(position = 3, example = "2020-12-30")
    private Date executeDate;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Category category;

}
