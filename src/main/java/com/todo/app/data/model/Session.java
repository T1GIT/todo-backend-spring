package com.todo.app.data.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.todo.app.data.util.base.AuditModel;
import com.todo.app.security.Validator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;


@Schema(description = "Representation of the sessions table")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sessions")
public class Session extends AuditModel<Session> {

    @Schema(example = "WfLf40GtRol24T7NDNtC")
    @NotBlank
    @Size(min = 10, max = 50)
    @Pattern(regexp = Validator.FINGERPRINT_PATTERN)
    @Column(nullable = false, unique = true, length = 50)
    protected String fingerprint;

    @Schema(example = "fihefUHFUe7EFhuh8987HOfheuhEUg38Hefhus802efF")
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private String refresh;

    @Schema(example = "2022-12-31",
            accessMode = Schema.AccessMode.WRITE_ONLY)
    @Column(nullable = false)
    private Date expires;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User user;

}
