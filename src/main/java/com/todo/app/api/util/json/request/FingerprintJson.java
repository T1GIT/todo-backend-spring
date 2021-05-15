package com.todo.app.api.util.json.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.todo.app.data.model.Session;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;


@ApiModel
public class FingerprintJson extends LoginFormJson {

    @JsonIgnore @Override
    public void setEmail(String email) {
        super.setEmail(email);
    }

    @JsonIgnore @Override
    public void setPsw(String psw) {
        super.setPsw(psw);
    }
}
