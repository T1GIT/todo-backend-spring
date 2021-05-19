package com.todo.app.api.util.json.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Schema
public class JwtJson implements Serializable {

    @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiZW1haWwiOiJleGFtcGxlQG1haWwucnUiLCJwc3ciOiJwYXNzd29yZDEiLCJpYXQiOjE1MTYyMzkwMjJ9.IpsL7WJp9VoCm6fj8Ns8qZ4tzIn4D29QEKle26Qx4Wk")
    @NotNull
    private String jwt;
}
