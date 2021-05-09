package com.todo.app.data.service;

import com.todo.app.Main;
import com.todo.app.data.exception.ResourceNotFoundException;
import com.todo.app.data.model.Category;
import com.todo.app.data.model.Task;
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
import java.util.Date;
import java.util.List;

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
class TaskServiceTest {

    static User user;
    static Category category;
    static String title = "some title";

    @Autowired UserService userService;
    @Autowired CategoryService categoryService;
    @Autowired TaskService taskService;

    @BeforeEach
    void beforeEach() {
        user = userService.register(new User()
                .edit(u -> {
                    u.setEmail("example@mail.com");
                    u.setPsw("some password");
                }));
        category = categoryService.add(user.getId(), new Category()
                .edit(c -> c.setName("category")));
    }

    @AfterEach
    void afterEach() {
        userService.delete(user.getId());
    }

    @Test
    void getOf() {
        insertTasks(category.getId(), 10);
        List<Task> tasks = taskService.getOf(category.getId());
        List<Task> sortedTasks = new ArrayList<>(tasks);
        sortedTasks.sort((c1, c2) -> String.CASE_INSENSITIVE_ORDER.compare(c1.getTitle(), c2.getTitle()));
        assertEquals(sortedTasks, tasks);
        System.out.println(tasks);
    }


    @Test
    void add() {
        int amount = 100;
        insertTasks(category.getId(), amount);
        List<Task> tasks = taskService.getOf(category.getId());
        assertEquals(amount, tasks.size());
        for (Task task: tasks)
            assertEquals(title, task.getTitle());
        System.out.println(tasks);
    }

    @Test
    void edit() {
        String title = "Some new title";
        String desc = "Description";
        Date date = new Date();
        int amount = 100;
        insertTasks(category.getId(), amount);
        for (Task task: taskService.getOf(category.getId())) {
            taskService.update(task.getId(), t -> {
                t.setTitle(title);
                t.setDescription(desc);
                t.setCompleted(true);
                t.setExecuteDate(date);
            });
        }
        List<Task> tasks = taskService.getOf(category.getId());
        assertEquals(amount, tasks.size());
        for (Task task: tasks) {
            assertEquals(title, task.getTitle());
            assertEquals(desc, task.getDescription());
            assertTrue(task.isCompleted());
            assertEquals(date, task.getExecuteDate());
        }
        System.out.println(tasks);
    }

    @Test
    void delete() {
        Task task = taskService.add(category.getId(), new Task()
                .edit(c -> c.setTitle(title)));
        taskService.delete(task.getId());
        assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.delete(task.getId()));
    }

    @Test
    void sortSpeed() {
        int amount = 1000;
        insertTasks(category.getId(), amount);
        for (int i = 0; i < amount; i++) {
            taskService.getOf(category.getId());
        }
    }

    private void insertTasks(long categoryId, int amount) {
        for (int i = 0; i < amount; i++) {
            taskService.add(categoryId, new Task()
                    .edit(t -> t.setTitle(title)));
        }
    }
}