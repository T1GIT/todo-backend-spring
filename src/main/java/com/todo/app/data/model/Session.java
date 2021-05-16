package com.todo.app.data.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.todo.app.data.util.base.AuditModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.Example;
import lombok.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;


@ApiModel
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sessions")
public class Session extends AuditModel<Session> {

    @ApiModelProperty(position = 0, example = "fihefUHFUe7EFhuh8987HOfheuhEUg38Hefhus802efF")
    @NotNull
    @Column(nullable = false, unique = true)
    private String refresh;

    @ApiModelProperty(position = 1, example = "") // TODO: Add example and size
    @Size(max = 32, min = 32)
    @NotNull
    @Column(nullable = false, unique = true, length = 32)
    protected String fingerprint;

    @ApiModelProperty(position = 2, example = "2020") // TODO: Add example and size
    @NotNull
    @Column(nullable = false, unique = true)
    private Date expires;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User user;

}
