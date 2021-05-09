package com.todo.app.data.service;

import com.todo.app.Main;
import com.todo.app.data.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.transaction.Transactional;


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Main.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application_test.properties")
@EnableTransactionManagement
@EnableAutoConfiguration
@Transactional
class TaskServiceTest {

    static User user;

    @Autowired UserService userService;


    @BeforeTestClass
    public void beforeAll() {
        user = userService.register(new User()
                .edit(eUser -> {
                    eUser.setEmail("example@mail.com");
                    eUser.setPsw("some password");
                }));
    }

    @BeforeEach
    void beforeEach() {

    }

    @AfterEach
    void afterEach() {
        if (user != null) userService.delete(user.getId());
    }

    @Test
    void getOf() {
    }

    @Test
    void add() {
    }

    @Test
    void edit() {
    }

    @Test
    void delete() {
    }
}