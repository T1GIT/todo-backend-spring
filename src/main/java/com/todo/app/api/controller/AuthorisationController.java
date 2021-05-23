package com.todo.app.api.controller;

import com.todo.app.api.util.base.IncorrectFormException;
import com.todo.app.api.util.json.request.AuthForm;
import com.todo.app.api.util.json.response.JwtJson;
import com.todo.app.data.util.exception.EmailExistsException;
import com.todo.app.data.util.exception.EmailNotExistsException;
import com.todo.app.data.util.exception.ResourceNotFoundException;
import com.todo.app.security.util.exception.ExpiredRefreshException;
import com.todo.app.security.util.exception.InvalidFingerprintException;
import com.todo.app.security.util.exception.InvalidPswException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Tag(name = "Authorisation controller",
        description = "Controller to provide an authorisation process")
@RequestMapping("/authorisation")
public interface AuthorisationController {

    @Operation(
            description = "Adds user info in the database, creates session " +
                    "and attaches session refresh token to the cookies, responses JWT")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Form for receiving user's principles from the client",
            content = @Content(
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
            @ApiResponse(responseCode = "409", description = "Given email does already exist", content = @Content),
            @ApiResponse(responseCode = "422", description = "Some of the credentials is incorrect", content = @Content)
    })
    @PostMapping(
            value = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    JwtJson register(
            @RequestBody AuthForm form,
            HttpServletResponse response)
            throws EmailExistsException, IncorrectFormException;

    @Operation(
            description = "Creates session and attaches session refresh to" +
                    "the cookies, responses JWT")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Form for receiving user's principles from the client",
            content = @Content(
                    examples = @ExampleObject(
                            name = "Default login form",
                            description = "Gives email, password and fingerprint",
                            value = """
                                      {
                                      "fingerprint": "WfLf40GtRol24T7NDNtC",
                                      "user": {
                                        "email": "example@mail.ru",
                                        "psw": "password1"
                                      }
                                    }"""
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User was login"),
            @ApiResponse(responseCode = "422", description = "Some of the credentials is incorrect", content = @Content),
            @ApiResponse(responseCode = "404", description = "Given email does not exist", content = @Content),
            @ApiResponse(responseCode = "401", description = "Password/fingerprint is invalid for this account", content = @Content)
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(
            value = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    JwtJson login(
            @RequestBody AuthForm form,
            HttpServletResponse response)
            throws EmailNotExistsException, InvalidPswException, InvalidFingerprintException;


    @Operation(
            description = "Erase all the information about the current authorisation session")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "All session data was cleaned")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(
            value = "/logout")
    void logout(HttpServletRequest request, HttpServletResponse response);

    @Operation(
            description = "Gets fingerprint and the refresh token from the cookie, " +
                    "creates new JWT and refresh token? and responses with new JWT, " +
                    "attaches new refresh token to the cookies")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "JSON object with fingerprint",
            content = @Content(
                    examples = @ExampleObject(
                            name = "Fingerprint",
                            value = """
                                      {
                                      "fingerprint": "WfLf40GtRol24T7NDNtC"
                                    }"""
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tokens were successfully updated"),
            @ApiResponse(responseCode = "401", description = "Fingerprint is invalid or refresh token was expired", content = @Content),
            @ApiResponse(responseCode = "404", description = "Refresh token was not found in the database", content = @Content)
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            value = "/refresh",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    JwtJson refresh(
            @RequestBody AuthForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws ResourceNotFoundException, InvalidFingerprintException, ExpiredRefreshException;
}
