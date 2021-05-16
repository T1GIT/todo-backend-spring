package com.todo.app.data.service;

import com.todo.app.TodoApplication;
import com.todo.app.data.util.exception.ResourceNotFoundException;
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


@SpringBootTest(classes = TodoApplication.class)
@TestPropertySource("classpath:application_test.properties")
class TaskServiceTest {

    static User user;
    static Category category;
    static String title = "some task title";

    @Autowired UserService userService;
    @Autowired CategoryService categoryService;
    @Autowired TaskService taskService;

    @BeforeEach
    void beforeEach() {
        user = userService.register(new User()
                .edit(u -> {
                    u.setEmail("example@email.com");
                    u.setPsw("some password");
                    category = new Category()
                            .edit(c -> c.setName("category"));
                    u.addCategory(category);
                }));
    }

    @AfterEach
    void afterEach() {
        userService.delete(user.getId());
    }

    @Test
    void getOf() {
        insertTasks(category.getId(), 10);
        List<Task> tasks = taskService.getOf(user.getId(), category.getId());
        List<Task> sortedTasks = new ArrayList<>(tasks);
        sortedTasks.sort((c1, c2) -> String.CASE_INSENSITIVE_ORDER.compare(c1.getTitle(), c2.getTitle()));
        assertEquals(sortedTasks, tasks);
        System.out.println(tasks);
    }


    @Test
    void add() {
        int amount = 100;
        insertTasks(category.getId(), amount);
        List<Task> tasks = taskService.getOf(user.getId(), category.getId());
        assertEquals(amount, tasks.size());
        for (Task task: tasks)
            assertEquals(title, task.getTitle());
        System.out.println(tasks);
    }

    @Test
    void edit() {
        String title = "some new title";
        String desc = "some description";
        int amount = 100;
        insertTasks(category.getId(), amount);
        for (Task task: taskService.getOf(user.getId(), category.getId())) {
            taskService.update(user.getId(), task.getId(), new Task().edit(t -> {
                t.setTitle(title);
                t.setDescription(desc);
            }));
        }
        List<Task> tasks = taskService.getOf(user.getId(), category.getId());
        assertEquals(amount, tasks.size());
        for (Task task: tasks) {
            assertEquals(title, task.getTitle());
            assertEquals(desc, task.getDescription());
        }
        System.out.println(tasks);
    }

    @Test
    void delete() {

        Category category = categoryService.add(user.getId(), new Category()
                .edit(c -> c.setName("category")));
        Task task = taskService.add(user.getId(), category.getId(), new Task()
                .edit(c -> c.setTitle(title)));
        taskService.delete(user.getId(), task.getId());
        assertDoesNotThrow(() -> taskService.delete(user.getId(), task.getId()));
        System.out.println(task);
    }

    @Test
    void sortSpeed() {
        int amount = 100;
        insertTasks(category.getId(), amount);
        for (int i = 0; i < amount; i++) {
            taskService.getOf(user.getId(), category.getId());
        }
    }

    private void insertTasks(long categoryId, int amount) {
        for (int i = 0; i < amount; i++) {
            taskService.add(user.getId(), categoryId, new Task()
                    .edit(t -> t.setTitle(title)));
        }
    }
}