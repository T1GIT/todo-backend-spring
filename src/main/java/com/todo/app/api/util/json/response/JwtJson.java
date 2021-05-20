package com.todo.app.api.util.json.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Schema(description = "JSON object for sending JWT")
@Data
public class JwtJson implements Serializable {

    @Schema(example = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE1MTYyMzkwMjJ9.tCf7xgMgzyICPepwICOMPvUXycj_omP0EPCvf1IVWcc")
    @NotNull
    private String jwt;
}
