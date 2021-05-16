package com.todo.app.api.util.json.request;

import com.todo.app.data.model.User;
import com.todo.app.security.Validator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;


@Data
@ApiModel
public class AuthForm implements Serializable {

    @ApiModelProperty(example = "WfLf40GtRol24T7NDNtC")
    @NotNull
    @Size(min = 20, max = 20)
    @Pattern(regexp = Validator.FINGERPRINT_PATTERN)
    private String fingerprint;

    private User user;
}
