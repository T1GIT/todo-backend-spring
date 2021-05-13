package com.todo.app.data.service;

import com.todo.app.TodoApplication;
import com.todo.app.data.model.Category;
import com.todo.app.data.model.Refresh;
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


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = TodoApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application_test.properties")
@EnableTransactionManagement
@EnableAutoConfiguration
@Transactional
class RefreshServiceTest {

    static User user;

    @Autowired UserService userService;
    @Autowired RefreshService refreshService;

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
        Refresh refresh = refreshService.create(user.getId());
        System.out.println(refresh);
        assertNotNull(refresh);
        assertEquals(KeyLength.REFRESH.getLength(), refresh.getValue().length());
    }

    @Test
    void update() {
        Refresh refresh = refreshService.create(user.getId());
        String oldValue = refresh.getValue();
        Refresh newRefresh = refreshService.update(refresh.getValue());
        assertNotEquals(oldValue, newRefresh.getValue());
        assertEquals(KeyLength.REFRESH.getLength(), newRefresh.getValue().length());
    }

    @Test
    void delete() {
        Refresh refresh = refreshService.create(user.getId());
        refreshService.delete(refresh.getValue());
        assertDoesNotThrow(() -> refreshService.delete(refresh.getValue()));
    }
}