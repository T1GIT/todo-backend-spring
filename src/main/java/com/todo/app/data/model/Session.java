package com.todo.app.data.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.todo.app.data.util.base.AuditModel;
import com.todo.app.security.Validator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
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
    @Column(nullable = false, unique = true)
    private String refresh;

    @ApiModelProperty(position = 1, example = "WfLf40GtRol24T7NDNtC") // TODO: Add example and size
    @NotBlank
    @Size(min = 10, max = 50)
    @Pattern(regexp = Validator.FINGERPRINT_PATTERN)
    @Column(nullable = false, unique = true, length = 50)
    protected String fingerprint;

    @ApiModelProperty(position = 2, example = "2022-12-31") // TODO: Add example and size
    @Column(nullable = false)
    private Date expires;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User user;

}
