package com.todo.app.api.controller;

import com.google.gson.Gson;
import com.sun.tools.jconsole.JConsoleContext;
import com.todo.app.TodoApplication;
import com.todo.app.api.util.json.request.AuthForm;
import com.todo.app.api.util.json.response.JwtJson;
import com.todo.app.data.model.Category;
import com.todo.app.data.model.User;
import com.todo.app.data.repo.CategoryRepository;
import com.todo.app.data.repo.UserRepository;
import com.todo.app.security.token.RefreshProvider;
import org.checkerframework.checker.units.qual.C;
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
class CategoryControllerTest {

    static String email = "example@mail.ru";

    static String name = "name";

    static String jwt;

    static Gson parser = new Gson();

    @Autowired
    MockMvc mvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;

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
                .andDo(mvcResult -> {
                    jwt = parser.fromJson(
                            mvcResult.getResponse().getContentAsString(), JwtJson.class)
                            .getJwt();
                    System.out.println(jwt);
                });
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    void getCategoriesByUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/todo/categories")
                .header("authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void addCategory() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post("/todo/categories")
                .header("authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(createCategoryJson(name)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("location"))
                .andReturn();
        String location = result.getResponse().getHeader("location");
        long categoryId = Long.parseLong(location.substring(location.lastIndexOf("/") + 1));
        long userId = userRepository.findByEmail(email).get().getId();
        Category category = categoryRepository.getOne(categoryId);
        assertEquals(userId, category.getUser().getId());
        assertNotNull(category);
        assertEquals(name, category.getName());
    }

    @Test
    void changeName() throws Exception {
        String newName = "new " + name;
        Category category = new Category();
        category.setName(name);
        category.setUser(userRepository.getByEmail(email));
        categoryRepository.saveAndFlush(category);
        mvc.perform(MockMvcRequestBuilders
                .patch("/todo/categories/" + category.getId() + "/name")
                .header("authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(createCategoryJson(newName)))
                .andExpect(status().isNoContent());
        category = categoryRepository.getOne(category.getId());
        assertEquals(newName, category.getName());
    }

    @Test
    void deleteCategory() throws Exception {
        Category category = new Category();
        category.setName(name);
        category.setUser(userRepository.getByEmail(email));
        categoryRepository.saveAndFlush(category);
        mvc.perform(MockMvcRequestBuilders
                .delete("/todo/categories/" + category.getId())
                .header("authorization", "Bearer " + jwt))
                .andExpect(status().isNoContent());
        assertFalse(categoryRepository.existsById(category.getId()));
        mvc.perform(MockMvcRequestBuilders
                .delete("/todo/category/" + category.getId())
                .header("authorization", "Bearer " + jwt))
                .andExpect(status().isNotFound());
    }

    String createCategoryJson(String name) {
        return parser.toJson(new Category()
                .edit(c -> {
                    c.setName(name);
                }));
    }
}