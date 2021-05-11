package com.todo.app.data.service;

import com.todo.app.TodoApplication;
import com.todo.app.data.model.Task;
import com.todo.app.data.util.exception.ResourceNotFoundException;
import com.todo.app.data.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = TodoApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application_test.properties")
@EnableTransactionManagement
@EnableAutoConfiguration
@Transactional
class UserServiceTest {

    static User user;
    static String email = "example@email.ru";
    static String psw = "some password";
    static String name = "some user name";

    @Autowired
    UserService userService;

    @BeforeEach
    void beforeEach() {
        user = userService.register(new User()
                .edit(eUser -> {
                    eUser.setEmail(email);
                    eUser.setPsw(psw);
                    eUser.setName(name);
                }));
    }

    @AfterEach
    void afterEach() {
        if (user != null) {
            System.out.println(user);
            userService.delete(user.getId());
        }
    }

    @Test
    void register() {
        assertEquals(email, user.getEmail());
        assertNotEquals(psw, user.getPsw());
        assertEquals(1181, user.getPsw().length());
        assertTrue(user.getPsw().matches("^.*:\\d*(:[a-z0-9]*){2}"));
    }

    @Test
    void login() {
        User user = userService.login(new User()
                .edit(eUser -> {
                    eUser.setEmail(email);
                    eUser.setPsw(psw);
                }));
        assertEquals(name, user.getName());
    }

    @Test
    void changeEmail() {
        String newEmail = "new_" + email;
        userService.changeEmail(user.getId(), newEmail);
        User loginUser = userService.login(new User()
                .edit(eUser -> {
                    eUser.setEmail(newEmail);
                    eUser.setPsw(psw);
                }));
        assertNotNull(loginUser);
    }

    @Test
    void changePsw() {
        String newPsw = "new_" + psw;
        userService.changePsw(user.getId(), newPsw);
        User loginUser = userService.login(new User()
                .edit(eUser -> {
                    eUser.setEmail(email);
                    eUser.setPsw(newPsw);
                }));
        assertNotNull(loginUser);
    }

    @Test
    void edit() {
        String newName = "some another name";
        userService.update(user.getId(), new User().edit(u -> {
            u.setName(newName);
        }));
        user = userService.login(new User()
                .edit(eUser -> {
                    eUser.setEmail(email);
                    eUser.setPsw(psw);
                }));
        assertEquals(newName, user.getName());
    }

    @Test
    void delete() {
        userService.delete(user.getId());
        assertDoesNotThrow(
                () -> userService.delete(user.getId()));
        user = null;
    }
}