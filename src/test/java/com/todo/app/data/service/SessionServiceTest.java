package com.todo.app.data.service;

import com.todo.app.TodoApplication;
import com.todo.app.data.model.Session;
import com.todo.app.data.model.User;
import com.todo.app.security.util.enums.KeyLength;
import org.junit.jupiter.api.AfterEach;
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


@SpringBootTest(classes = TodoApplication.class)
@TestPropertySource("classpath:application_test.properties")
class SessionServiceTest {

    static User user;
    static String fingerprint = "f".repeat(32);

    @Autowired UserService userService;
    @Autowired SessionService sessionService;

    @BeforeEach
    void beforeEach() {
        user = userService.register(new User().edit(u -> {
            u.setEmail("example@mail.ru");
            u.setPsw("some password1");
        }));
    }

    @AfterEach
    void afterEach() {
        userService.delete(user.getId());
    }

    @Test
    void create() {
        Session session = sessionService.create(user.getId(), fingerprint);
        assertNotNull(session);
        assertEquals(KeyLength.REFRESH.getLength(), session.getRefresh().length());
        System.out.println(session);
    }

    @Test
    void update() {
        Session session = sessionService.create(user.getId(), fingerprint);
        String oldValue = session.getRefresh();
        Session newSession = sessionService.update(session.getRefresh(), fingerprint);
        assertNotEquals(oldValue, newSession.getRefresh());
        assertEquals(KeyLength.REFRESH.getLength(), newSession.getRefresh().length());
        System.out.println(session);
    }

    @Test
    void delete() {
        Session session = sessionService.create(user.getId(), fingerprint);
        sessionService.delete(session.getRefresh());
        assertDoesNotThrow(() -> sessionService.delete(session.getRefresh()));
        System.out.println(session);
    }
}