package com.todo.app.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.ApiSelector;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig extends WebMvcConfigurationSupport {

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(metaData())
                .securityContexts(securityContexts())
                .securitySchemes(apiKeys())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.todo.app.api.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("TODO REST API")
                .description("This is server API allows creating users, theirs categories and tasks")
                .version("v0.1")
                .license("Apache License Version 2.0") // TODO: put license type
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0\"") // TODO: put license url
                .contact(new Contact("Dmitriy Derbin", "http://vk.com/t1monvk", "derbindima5@gmail.com"))
                .termsOfServiceUrl("Все права защищены (нет)")
                .build();
    }

    private List<ApiKey> apiKeys() {
        return Collections.singletonList(
                new ApiKey("JWT", "Authorisation", "header"));
    }

    private List<SecurityContext> securityContexts() {
        return Collections.singletonList(
                SecurityContext.builder().securityReferences(defaultAuth()).build());
    }

    private List<SecurityReference> defaultAuth() {
        return Collections.singletonList(
                new SecurityReference(
                        "JWT",
                        new AuthorizationScope[]{
                                new AuthorizationScope("BASIC", "access TODO api"),
                                new AuthorizationScope("ADMIN", "access administrative functionality")}));
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
