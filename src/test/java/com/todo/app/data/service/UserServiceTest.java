package com.todo.app.data.service;

import com.todo.app.TodoApplication;
import com.todo.app.data.model.User;
import com.todo.app.data.util.exception.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = TodoApplication.class)
@TestPropertySource("classpath:application_test.properties")
@Transactional
class UserServiceTest {

    static User user;
    static String email = "example@email.ru";
    static String psw = "some password";
    static String name = "some user name";

    @Autowired UserService userService;

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
        try {
            userService.delete(user.getId());
        } catch (ResourceNotFoundException ignored) {
        }
    }

    @Test
    void register() {
        assertEquals(email, user.getEmail());
        assertNotEquals(psw, user.getPsw());
        assertEquals(1181, user.getPsw().length());
        assertTrue(user.getPsw().matches("^.*:\\d*(:[a-z0-9]*){2}"));
        System.out.println(user);
    }

    @Test
    void login() {
        User user = userService.login(new User()
                .edit(u -> {
                    u.setEmail(email);
                    u.setPsw(psw);
                }));
        assertEquals(name, user.getName());
        System.out.println(user);
    }

    @Test
    void changeEmail() {
        String newEmail = "new_" + email;
        userService.changeEmail(user.getId(), newEmail);
        User loginUser = userService.login(new User()
                .edit(u -> {
                    u.setEmail(newEmail);
                    u.setPsw(psw);
                }));
        assertNotNull(loginUser);
        System.out.println(user);
    }

    @Test
    void changePsw() {
        String newPsw = "new_" + psw;
        userService.changePsw(user.getId(), newPsw);
        User loginUser = userService.login(new User()
                .edit(u -> {
                    u.setEmail(email);
                    u.setPsw(newPsw);
                }));
        assertNotNull(loginUser);
        System.out.println(loginUser);
    }

    @Test
    void edit() {
        String newName = "some another name";
        userService.update(
                user.getId(),
                new User().edit(u -> {
                    u.setName(newName);
                }));
        user = userService.login(new User()
                .edit(u -> {
                    u.setEmail(email);
                    u.setPsw(psw);
                }));
        assertEquals(newName, user.getName());
        System.out.println(user);
    }

    @Test
    void delete() {
        userService.delete(user.getId());
        assertDoesNotThrow(() -> userService.delete(user.getId()));
    }
}