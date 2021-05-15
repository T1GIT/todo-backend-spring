package com.todo.app.api.util.json.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.todo.app.data.model.User;
import com.todo.app.security.util.enums.Role;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.Size;
import java.util.Date;


@ApiModel
public class LoginFormJson extends RegisterFormJson {

    @JsonIgnore @Override
    public void setName(String name) {
        super.setName(name);
    }

    @JsonIgnore @Override
    public void setSurname(String surname) {
        super.setSurname(surname);
    }

    @JsonIgnore @Override
    public void setPatronymic(String patronymic) {
        super.setPatronymic(patronymic);
    }

    @JsonIgnore @Override
    public void setBirthdate(Date birthdate) {
        super.setBirthdate(birthdate);
    }
}
