package com.todo.app.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
public class SwaggerConfig {

    public static final String SECURITY_SCHEME = "JWT authentication";

    @Bean
    public OpenAPI apiInfo(@Value("${server.servlet.context-path}") String contextPath) {
        return new OpenAPI()
                .components(apiJwtAuth())
                .info(apiInfo())
                .servers(apiServers(contextPath));
    }

    @Bean
    public GroupedOpenApi authorisationApi() {
        return GroupedOpenApi.builder()
                .group("Authorisation")
                .pathsToMatch(
                        "/authorisation/**",
                        "/user/**")
                .build();
    }

    @Bean
    public GroupedOpenApi AdminApi() {
        return GroupedOpenApi.builder()
                .group("Administration")
                .pathsToMatch("/admin/**")
                .build();
    }

    @Bean
    public GroupedOpenApi TodoApi() {
        return GroupedOpenApi.builder()
                .group("Todo")
                .pathsToMatch("/todo/**")
                .build();
    }

    private Info apiInfo() {
        return new Info()
                .title("TODO REST API")
                .description("Restful api for creating tasks")
                .version("v0.1")
                .license(new License()
                        .name("GPL-3.0")
                        .url("https://github.com/T1GIT/todo-backend-spring/blob/master/LICENSE.md")) // TODO: Add license
                .contact(new Contact()
                        .name("Derbin Dmitriy")
                        .email("derbindima5@gmail.com")
                        .url("https://github.com/t1git"));
    }

    private Components apiJwtAuth() {
        return new Components()
                .addSecuritySchemes(
                        SECURITY_SCHEME,
                        new SecurityScheme()
                                .name(SECURITY_SCHEME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                );
    }

    private List<Server> apiServers(String contextPath) {
        return List.of(
                new Server()
                        .url("http://localhost:8080" + contextPath)
                        .description("Dev server"));
    }
}
