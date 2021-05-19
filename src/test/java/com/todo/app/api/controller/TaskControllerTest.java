package com.todo.app.api.controller;

import com.google.gson.Gson;
import com.todo.app.TodoApplication;
import com.todo.app.api.util.json.request.AuthForm;
import com.todo.app.api.util.json.response.JwtJson;
import com.todo.app.data.model.Category;
import com.todo.app.data.model.Task;
import com.todo.app.data.model.User;
import com.todo.app.data.repo.CategoryRepository;
import com.todo.app.data.repo.TaskRepository;
import com.todo.app.data.repo.UserRepository;
import com.todo.app.security.token.RefreshProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = TodoApplication.class)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application_test.properties")
@Transactional
class TaskControllerTest {

    static String email = "example@mail.ru";
    static long categoryId;

    static String title = "title";
    static String desc = "desc";

    static String jwt;

    static Gson parser = new Gson();

    @Autowired
    MockMvc mvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    TaskRepository taskRepository;

    @BeforeEach
    void beforeEach() throws Exception {
        AuthForm authForm = new AuthForm();
        authForm.setFingerprint("WfLf40GtRol24T7NDNtC");
        authForm.setUser(new User().edit(u -> {
            u.setEmail(email);
            u.setPsw("password1");
        }));
        mvc.perform(MockMvcRequestBuilders
                .post("/authorisation/register")
                .content(parser.toJson(authForm, AuthForm.class))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.jwt").exists())
                .andExpect(cookie().exists(RefreshProvider.COOKIE_NAME))
                .andDo(mvcResult -> jwt = parser.fromJson(
                        mvcResult.getResponse().getContentAsString(), JwtJson.class)
                        .getJwt());
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post("/todo/category")
                .header("authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(parser.toJson(new Category().edit(c -> {
                    c.setName("category");
                }))))
                .andExpect(status().isCreated())
                .andExpect(header().exists("location"))
                .andReturn();
        String location = result.getResponse().getHeader("location");
        categoryId = Long.parseLong(location.substring(location.lastIndexOf("/") + 1));
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    void getTasksByCategory() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/todo/category/" + categoryId + "/tasks")
                .header("authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(
                        categoryRepository.getOne(categoryId)
                                .getTasks().size()
                ));
    }

    @Test
    void addTask() throws Exception {
        long taskId = createTaskByRequest();
        Task task = taskRepository.getOne(taskId);
        assertNotNull(task);
        assertEquals(title, task.getTitle());
        assertEquals(desc, task.getDescription());
    }

    @Test
    void updateTask() throws Exception {
        String newTitle = "new " + title;
        String newDesc = "new " + desc;
        long taskId = createTaskByRequest();
        mvc.perform(MockMvcRequestBuilders
                .put("/todo/task/" + taskId)
                .header("authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(parser.toJson(new Task().edit(t -> {
                    t.setTitle(newTitle);
                    t.setDescription(newDesc);
                }))))
                .andExpect(status().isNoContent());
        Task task = taskRepository.getOne(taskId);
        assertEquals(newTitle, task.getTitle());
        assertEquals(newDesc, task.getDescription());
    }

    @Test
    void changeCompleted() throws Exception {
        long taskId = createTaskByRequest();
        mvc.perform(MockMvcRequestBuilders
                .patch("/todo/task/" + taskId + "/completed")
                .header("authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(parser.toJson(new Task().edit(t -> {
                    t.setCompleted(true);
                }))))
                .andExpect(status().isNoContent());
        Task task = taskRepository.getOne(taskId);
        assertTrue(task.isCompleted());
        assertNotNull(task.getExecuteDate());
        mvc.perform(MockMvcRequestBuilders
                .patch("/todo/task/" + taskId + "/completed")
                .header("authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(parser.toJson(new Task().edit(t -> {
                    t.setCompleted(false);
                }))))
                .andExpect(status().isNoContent());
        task = taskRepository.getOne(taskId);
        assertFalse(task.isCompleted());
        assertNull(task.getExecuteDate());
    }

    @Test
    void deleteTask() throws Exception {
        long taskId = createTaskByRequest();
        mvc.perform(MockMvcRequestBuilders
                .delete("/todo/task/" + taskId)
                .header("authorization", "Bearer " + jwt))
                .andExpect(status().isNoContent());
        assertFalse(taskRepository.existsById(taskId));
        mvc.perform(MockMvcRequestBuilders
                .delete("/todo/task/" + taskId)
                .header("authorization", "Bearer " + jwt))
                .andExpect(status().isNoContent());
    }

    long createTaskByRequest() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post("/todo/category/" + categoryId + "/task")
                .header("authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(parser.toJson(new Task().edit(t -> {
                    t.setTitle(title);
                    t.setDescription(desc);
                }))))
                .andExpect(status().isCreated())
                .andReturn();

        String location = result.getResponse().getHeader("location");
        return Long.parseLong(location.substring(location.lastIndexOf("/") + 1));
    }
}