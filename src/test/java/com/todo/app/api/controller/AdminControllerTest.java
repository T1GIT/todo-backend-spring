package com.todo.app.api.controller;

import com.google.gson.Gson;
import com.todo.app.TodoApplication;
import com.todo.app.api.util.json.request.AuthForm;
import com.todo.app.api.util.json.response.JwtJson;
import com.todo.app.data.model.User;
import com.todo.app.data.repo.UserRepository;
import com.todo.app.security.token.JwtProvider;
import com.todo.app.security.token.RefreshProvider;
import com.todo.app.security.util.enums.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = TodoApplication.class)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application_test.properties")
class AdminControllerTest {


    static String email = "example@mail.ru";

    static String jwt;

    static Gson parser = new Gson();

    @Autowired
    MockMvc mvc;
    @Autowired
    UserRepository userRepository;

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
        userRepository.saveAndFlush(
                userRepository.findByEmail(email).map(user -> {
                    user.edit(u -> u.setRole(Role.ADMIN));
                    jwt = JwtProvider.getJwt(user);
                    return user;
                }).orElseThrow());
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    void updateJwtKey() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .post("/admin/update-jwt-key")
                .header("authorization", "Bearer " + jwt))
                .andExpect(status().isNoContent());
    }
}