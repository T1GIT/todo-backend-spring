package com.todo.app.api.util.json.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiIgnore
public class JwtJson implements Serializable {

    @ApiModelProperty(value = "JWT string", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiZW1haWwiOiJleGFtcGxlQG1haWwucnUiLCJwc3ciOiJwYXNzd29yZDEiLCJpYXQiOjE1MTYyMzkwMjJ9.IpsL7WJp9VoCm6fj8Ns8qZ4tzIn4D29QEKle26Qx4Wk")
    @NotNull
    private String jwt;
}
