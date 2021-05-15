package com.todo.app.api.util.json.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.todo.app.data.model.Session;
import com.todo.app.data.model.User;
import com.todo.app.security.util.enums.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@ApiModel
public class RegisterFormJson extends User {

    @Getter @Setter
    private String fingerprint;

    @JsonIgnore @Override
    public void setId(long id) {
        super.setId(id);
    }

    @JsonIgnore @Override
    public void setRole(Role role) {
        super.setRole(role);
    }
}
