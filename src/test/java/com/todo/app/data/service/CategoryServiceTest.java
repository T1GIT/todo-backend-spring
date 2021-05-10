package com.todo.app.data.service;

import com.todo.app.TodoApplication;
import com.todo.app.data.util.exception.ResourceNotFoundException;
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
import java.util.List;

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
class CategoryServiceTest {

    static User user;
    static String name = "some category name";

    @Autowired UserService userService;
    @Autowired CategoryService categoryService;

    @BeforeEach
    void beforeEach() {
        user = userService.register(new User()
            .edit(eUser -> {
                eUser.setEmail("example@email.com");
                eUser.setPsw("some password");
            }));
    }

    @AfterEach
    void afterEach() {
        userService.delete(user.getId());
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
        int amount = 100;
        user.getCategories().forEach(user::removeCategory);
        insertCategories(user.getId(), amount);
        List<Category> categories = categoryService.getOf(user.getId());
        assertEquals(amount, categories.size());
        for (Category category: categories)
            assertEquals(name, category.getName());
        System.out.println(categories);
    }

    @Test
    void changeName() {
        String newName = "some new name";
        int amount = 100;
        user.getCategories().forEach(user::removeCategory);
        insertCategories(user.getId(), amount);
        for (Category category: categoryService.getOf(user.getId()))
            categoryService.changeName(category.getId(), newName);
        List<Category> categories = categoryService.getOf(user.getId());
        assertEquals(amount, categories.size());
        for (Category category: categories)
            assertEquals(newName, category.getName());
        System.out.println(categories);
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

    @Test
    void sortSpeed() {
        int amount = 1000;
        insertCategories(user.getId(), amount);
        for (int i = 0; i < amount; i++) {
            categoryService.getOf(user.getId());
        }
    }

    private void insertCategories(long userId, int amount) {
        for (int i = 0; i < amount; i++) {
            categoryService.add(userId, new Category()
                    .edit(t -> t.setName(name)));
        }
    }
}