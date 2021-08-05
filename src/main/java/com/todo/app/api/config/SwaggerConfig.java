package com.todo.app.api.config;

import com.todo.app.TodoApplication;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
public class SwaggerConfig {

    public static final String SECURITY_SCHEME = "JWT authentication";

    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Value("${server.port}")
    private String port;
    @Value("${springdoc.swagger-ui.path}")
    private String docsUrl;

    @Bean
    public void printLink() {
        System.out.printf("API documentation is on page: " +
                "%s:%s%s%s%n", TodoApplication.ADDRESS, port, contextPath, docsUrl);
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(apiJwtAuth())
                .info(apiInfo())
                .servers(apiServers());
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
                .version(contextPath.substring(contextPath.lastIndexOf("/") + 1))
                .license(new License()
                        .name("GPL-3.0")
                        .url("https://github.com/T1GIT/todo-backend-spring/blob/master/LICENSE.md"))
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

    private List<Server> apiServers() {
        return List.of(
                new Server()
                        .url("%s:%s%s".formatted(TodoApplication.ADDRESS, port, contextPath))
                        .description("Dev server"));
    }
}
