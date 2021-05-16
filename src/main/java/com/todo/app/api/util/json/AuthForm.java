package com.todo.app.api.util.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.todo.app.api.util.json.request.RegisterFormJson;
import com.todo.app.data.model.User;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;


@ApiModel
public class AuthForm implements Serializable {

    @Getter
    @Setter
    private String fingerprint;

    @Getter
    @Setter
    private User user;
}
