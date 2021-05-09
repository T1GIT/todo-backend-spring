package com.todo.app.data.service;

import com.todo.app.Main;
import com.todo.app.data.exception.ResourceNotFoundException;
import com.todo.app.data.model.Category;
import com.todo.app.data.model.User;
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Main.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application_test.properties")
@EnableTransactionManagement
@EnableAutoConfiguration
@Transactional
class CategoryServiceTest {

    static User user;
    static String name = "some name";

    @Autowired UserService userService;
    @Autowired CategoryService categoryService;

    @BeforeEach
    void beforeEach() {
        user = userService.register(new User()
            .edit(eUser -> {
                eUser.setEmail("example@mail.com");
                eUser.setPsw("some password");
            }));
    }

    @AfterEach
    void afterEach() {
        if (user != null) userService.delete(user.getId());
    }

    @Test
    void getOf() {
        List<Category> categories = categoryService.getOf(user.getId());
        List<Category> sortedCategories = new ArrayList<>(categories);
        sortedCategories.sort((c1, c2) -> String.CASE_INSENSITIVE_ORDER.compare(c1.getName(), c2.getName()));
        assertEquals(sortedCategories, categories);
        System.out.println(categories);
    }

    @Test
    void add() {
        int initAmount = user.getCategories().size();
        Category category = categoryService.add(user.getId(), new Category()
                .edit(c -> c.setName(name)));
        assertEquals(initAmount + 1, user.getCategories().size());
        System.out.println(category);
    }

    @Test
    void changeName() {
        String newName = "some new name";
        Category category = categoryService.add(user.getId(), new Category()
                .edit(c -> c.setName(name)));
        categoryService.changeName(category.getId(), newName);
        category = user.getCategories().parallelStream().filter(
                c -> c.getName().equals(newName)).findAny().orElse(null);
        System.out.println(category);
        assertNotNull(category);
    }

    @Test
    void delete() {
        Category category = categoryService.add(user.getId(), new Category()
            .edit(c -> c.setName(name)));
        categoryService.delete(category.getId());
        assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.delete(category.getId()));
    }
}