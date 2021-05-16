package com.todo.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableJpaAuditing
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class TodoApplication {

    public static final String DOMAIN = "localhost";
    public static final String ADDRESS = "http://" + DOMAIN;

    public static void main(String[] args) {
        SpringApplication.run(TodoApplication.class, args);
    }
}
