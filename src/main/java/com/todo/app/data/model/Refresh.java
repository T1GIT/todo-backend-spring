package com.todo.app.data.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.todo.app.data.util.base.AuditModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiIgnore
@Entity
@Table(name = "tokens")
public class Refresh extends AuditModel<Refresh> {

    @ApiModelProperty(position = 0, example = "fihefUHFUe7EFhuh8987HOfheuhEUg38Hefhus802efF")
    @NotNull
    @Column(nullable = false, unique = true)
    private String value;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

}
