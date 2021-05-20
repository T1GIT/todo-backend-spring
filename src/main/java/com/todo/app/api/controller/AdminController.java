package com.todo.app.api.controller;

import com.todo.app.api.config.SwaggerConfig;
import com.todo.app.data.model.User;
import com.todo.app.data.util.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Administrator controller",
        description = "Controller to execute administrative functions")
@SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME)
@RequestMapping("/admin")
public interface AdminController {

    @Operation(
            description = "Generates new JWT secret key. All the JWT becomes invalid after this")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "JWT key was updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorised access"),
            @ApiResponse(responseCode = "403", description = "Have no permissions")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/update-jwt-key")
    void updateJwtKey();

    @Operation(
            description = "Changes role of the given user")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "JSON object with new role",
            content = @Content(
                    examples = @ExampleObject(
                            name = "Role",
                            value = """
                                      {
                                      "role": "ADMIN",
                                    }"""
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Role was changed,"),
            @ApiResponse(responseCode = "401", description = "Unauthorised access"),
            @ApiResponse(responseCode = "403", description = "Have no permissions"),
            @ApiResponse(responseCode = "404", description = "Given user was not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/user/{userId}/role")
    void changeRole(
            @PathVariable long userId,
            @RequestBody User user)
            throws ResourceNotFoundException;
}
