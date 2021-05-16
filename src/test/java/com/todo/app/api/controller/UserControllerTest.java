package com.todo.app.api.controller;

import com.google.gson.Gson;
import com.todo.app.TodoApplication;
import com.todo.app.api.util.json.request.AuthForm;
import com.todo.app.api.util.json.response.JwtJson;
import com.todo.app.data.model.User;
import com.todo.app.data.repo.UserRepository;
import com.todo.app.security.crypt.Hash;
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
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = TodoApplication.class)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application_test.properties")
class UserControllerTest {

    static String email = "example@mail.ru";
    static String invalidEmail = "example mail.ru";
    static String anotherEmail = "example1@mail.ru";
    static String psw = "password1";
    static String invalidPsw = "password";
    static String anotherPsw = "password12";

    static String name = "Example name";
    static String surname = "Example surname";
    static String patronymic = "Example patronymic";

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
        authForm.setUser(createUser(0, 0));
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
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    void changeEmail() throws Exception {
        User[] cases = new User[]{
                createUser(0, 0),
                createUser(1, 0),
                createUser(2, 0),
                createUser(3, 0),
        };
        ResultMatcher[] expectations = new ResultMatcher[]{
                status().isConflict(),
                status().isUnprocessableEntity(),
                status().isNoContent(),
                status().isUnprocessableEntity()
        };
        for (int i = 0; i < cases.length; i++) {
            mvc.perform(MockMvcRequestBuilders
                    .patch("/user/email")
                    .header("authorization", "Bearer " + jwt)
                    .content(parser.toJson(cases[i], User.class))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(expectations[i]);
        }
        assertTrue(userRepository.findByEmail(email).isEmpty());
        assertFalse(userRepository.findByEmail(anotherEmail).isEmpty());
    }

    @Test
    void changePsw() throws Exception {
        User user = userRepository.findByEmail(email).get();
        assertTrue(Hash.check(psw, user.getPsw()));
        assertFalse(Hash.check(anotherPsw, user.getPsw()));
        User[] cases = new User[]{
                createUser(0, 0),
                createUser(0, 1),
                createUser(0, 2),
                createUser(0, 3),
        };
        ResultMatcher[] expectations = new ResultMatcher[]{
                status().isNoContent(),
                status().isUnprocessableEntity(),
                status().isNoContent(),
                status().isUnprocessableEntity()
        };
        for (int i = 0; i < cases.length; i++) {
            mvc.perform(MockMvcRequestBuilders
                    .patch("/user/psw")
                    .header("authorization", "Bearer " + jwt)
                    .content(parser.toJson(cases[i], User.class))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(expectations[i]);
        }
        user = userRepository.findByEmail(email).get();
        assertFalse(Hash.check(psw, user.getPsw()));
        assertTrue(Hash.check(anotherPsw, user.getPsw()));
    }

    @Test
    void updateUser() throws Exception {
        String prefix = "new ";
        User user = userRepository.findByEmail(email).get();
        assertEquals(name, user.getName());
        assertEquals(surname, user.getSurname());
        assertEquals(patronymic, user.getPatronymic());
        mvc.perform(MockMvcRequestBuilders
                .put("/user")
                .header("authorization", "Bearer " + jwt)
                .content(parser.toJson(new User().edit(u -> {
                    u.setName(prefix + name);
                    u.setSurname(prefix + surname);
                    u.setPatronymic(prefix + patronymic);
                }), User.class))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        user = userRepository.findByEmail(email).get();
        assertEquals(prefix + name, user.getName());
        assertEquals(prefix + surname, user.getSurname());
        assertEquals(prefix + patronymic, user.getPatronymic());
    }

    @Test
    void deleteUser() throws Exception {
        assertEquals(1, userRepository.count());
        mvc.perform(MockMvcRequestBuilders
                .delete("/user")
                .header("authorization", "Bearer " + jwt))
                .andExpect(status().isNoContent());
        assertNull(userRepository.findByEmail(email).orElse(null));
        assertEquals(0, userRepository.count());
    }

    User createUser(int emailType, int pswType) {
        return new User().edit(u -> {
            u.setEmail(switch (emailType) {
                case 0 -> email;
                case 1 -> invalidEmail;
                case 2 -> anotherEmail;
                case 3 -> null;
                default -> throw new IllegalStateException("Unexpected value: " + emailType);
            });
            u.setPsw(switch (pswType) {
                case 0 -> psw;
                case 1 -> invalidPsw;
                case 2 -> anotherPsw;
                case 3 -> null;
                default -> throw new IllegalStateException("Unexpected value: " + pswType);
            });
            u.setName(name);
            u.setSurname(surname);
            u.setPatronymic(patronymic);
        });
    }
}