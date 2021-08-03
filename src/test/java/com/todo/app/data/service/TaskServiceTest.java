package com.todo.app.data.service;

import com.todo.app.TodoApplication;
import com.todo.app.data.model.Category;
import com.todo.app.data.model.Task;
import com.todo.app.data.model.User;
import com.todo.app.data.repo.CategoryRepository;
import com.todo.app.data.repo.TaskRepository;
import com.todo.app.data.util.exception.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest(classes = TodoApplication.class)
@TestPropertySource("classpath:application_test.properties")
@Transactional
class TaskServiceTest {

    static User user;
    static long categoryId;
    static String title = "some task title";

    @Autowired CategoryRepository categoryRepository;
    @Autowired TaskRepository taskRepository;
    @Autowired UserService userService;
    @Autowired CategoryService categoryService;
    @Autowired TaskService taskService;

    @BeforeEach
    void beforeEach() {
        user = new User();
        user.setEmail("example@email.com");
        user.setPsw("some password");
        userService.register(user);
        Category category = new Category();
        category.setName("name");
        category.setUser(user);
        categoryRepository.saveAndFlush(category);
        categoryId = category.getId();
    }

    @AfterEach
    void afterEach() {
        try {
            userService.delete(user.getId());
        } catch (ResourceNotFoundException ignored) {
        }
    }

    @Test
    void getOf() {
        insertTasks(categoryId, 10);
        List<Task> tasks = taskService.getOf(user.getId(), categoryId);
        List<Task> sortedTasks = new ArrayList<>(tasks);
        sortedTasks.sort((c1, c2) -> String.CASE_INSENSITIVE_ORDER.compare(c1.getTitle(), c2.getTitle()));
        assertEquals(sortedTasks, tasks);
        System.out.println(tasks);
    }


    @Test
    void add() {
        int amount = 100;
        insertTasks(categoryId, amount);
        List<Task> tasks = taskService.getOf(user.getId(), categoryId);
        assertEquals(amount, tasks.size());
        for (Task task: tasks)
            assertEquals(title, task.getTitle());
        System.out.println(tasks);
    }

    @Test
    void edit() {
        String title = "some new title";
        String desc = "some new description";
        int amount = 100;
        insertTasks(categoryId, amount);
        for (Task task: taskService.getOf(user.getId(), categoryId)) {
            Task editedTask = new Task();
            editedTask.setTitle(title);
            editedTask.setDescription(desc);
            System.out.println(task.getCategory());
            taskService.update(user.getId(), categoryId, task.getId(), editedTask);
        }
        for (Task task: taskService.getOf(user.getId(), categoryId)) {
            assertEquals(title, task.getTitle());
            assertEquals(desc, task.getDescription());
        }
    }

    @Test
    void delete() {
        Category category = categoryService.add(user.getId(), new Category()
                .edit(c -> c.setName("category")));
        Task task = taskService.add(user.getId(), categoryId, new Task()
                .edit(c -> c.setTitle(title)));
        taskService.delete(user.getId(), categoryId, task.getId());
        assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.delete(user.getId(),categoryId, task.getId()));
        System.out.println(task);
    }

    protected void insertTasks(long categoryId, int amount) {
        Category category = categoryRepository.getOne(categoryId);
        for (int i = 0; i < amount; i++) {
            Task task = new Task();
            task.setCategory(category);
            task.setTitle(title);
            taskRepository.saveAndFlush(task);
        }
    }
}