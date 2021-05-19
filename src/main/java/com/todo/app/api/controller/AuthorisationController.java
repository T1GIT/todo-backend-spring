package com.todo.app.api.controller;

import com.todo.app.api.util.json.request.AuthForm;
import com.todo.app.api.util.json.response.JwtJson;
import com.todo.app.data.model.Session;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestControllerAdvice
@Tag(name = "Authorisation controller",
        description = "Controller to provide an authorisation process")
@RequestMapping("/authorisation")
public interface AuthorisationController {

    @Operation(description = "Registers the user and responses JWT")
    @RequestBody(
            description = "Form for receiving user's principles from the client",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = {
                            @ExampleObject(
                                    name = "Basic info",
                                    description = "Gives only required credentials",
                                    value = """
                                              {
                                              "fingerprint": "WfLf40GtRol24T7NDNtC",
                                              "user": {
                                                "email": "example@mail.ru",
                                                "psw": "password1"
                                              }
                                            }"""
                            ),
                            @ExampleObject(
                                    name = "Full info",
                                    description = "Gives all the credentials, that are possible",
                                    value = """
                                            {
                                              "fingerprint": "WfLf40GtRol24T7NDNtC",
                                              "user": {
                                                "email": "example@mail.ru",
                                                "psw": "password1",
                                                "name": "Ivan",
                                                "surname": "Ivanov",
                                                "patronymic": "Ivanovich",
                                                "birthdate": "2000-12-31"
                                              }
                                            }"""
                            )
                    }
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User was created"),
            @ApiResponse(responseCode = "422", description = "Some of the credentials is invalid"),
            @ApiResponse(responseCode = "409", description = "Given email does already exist")
    })
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    JwtJson register(@RequestBody AuthForm authForm, HttpServletResponse response);

    @RequestBody(
            description = "desc",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples =
                            {@ExampleObject(
                                    name = "NAAAAAAAAAme",
                                    description = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                                    value = """
                                            {
                                              "email": "example@mail.ru",
                                              "psw": "password1"
                                            }"""
                            )}
            ))
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    JwtJson login(
            AuthForm authForm,
            HttpServletResponse response);

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/logout")
    void logout(HttpServletRequest request, HttpServletResponse response);

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/refresh")
    JwtJson refresh(
            @RequestBody Session session,
            HttpServletRequest request, HttpServletResponse response);
}
