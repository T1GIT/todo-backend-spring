package com.todo.app.api.controller;

import com.google.gson.Gson;
import com.todo.app.TodoApplication;
import com.todo.app.api.util.json.request.AuthForm;
import com.todo.app.data.model.User;
import com.todo.app.data.repo.SessionRepository;
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
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.Cookie;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = TodoApplication.class)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application_test.properties")
class AuthorisationControllerTest {

    static String email = "example@mail.ru";
    static String invalidEmail = "example mail.ru";
    static String anotherEmail = "example1@mail.ru";
    static String psw = "password1";
    static String invalidPsw = "password";
    static String anotherPsw = "password12";
    static String fingerprint = "WfLf40GtRol24T7NDNtC";
    static String invalidFingerprint = "Wfefe2%$$#^Gt";
    static String anotherFingerprint = "afLf40Gt36424T7ND4Fa";

    static String refresh;

    static Gson parser = new Gson();

    @Autowired
    MockMvc mvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SessionRepository sessionRepository;

    @BeforeEach
    void beforeEach() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .post("/authorisation/register")
                .content(parseForm(0, 0, 0))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.jwt").exists())
                .andExpect(cookie().exists(RefreshProvider.COOKIE_NAME))
                .andDo(mvcResult -> refresh = mvcResult.getResponse()
                        .getCookie(RefreshProvider.COOKIE_NAME).getValue());
        assertEquals(1, userRepository.count());
//                .andDo(mvcResult -> jwt = parser.fromJson(
//                        mvcResult.getResponse().getContentAsString(), JwtJson.class)
//                        .getJwt());
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    void register() throws Exception {
        ArrayList<String> invalidCases = new ArrayList<>() {{
            add(parseForm(1, 0, 0));
            add(parseForm(0, 1, 0));
            add(parseForm(0, 0, 1));
            add(parseForm(0, 0, 0));
        }};
        ArrayList<ResultMatcher> expectations = new ArrayList<>() {{
            add(status().isUnprocessableEntity());
            add(status().isUnprocessableEntity());
            add(status().isUnprocessableEntity());
            add(status().isConflict());
        }};
        for (int i = 0; i < invalidCases.size(); i++) {
            System.out.println("CASE " + i);
            mvc.perform(MockMvcRequestBuilders
                    .post("/authorisation/register")
                    .content(invalidCases.get(i))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(expectations.get(i));
        }
        assertEquals(1, userRepository.count());
        assertEquals(1, sessionRepository.count());
    }

    @Test
    void login() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .post("/authorisation/login")
                .content(parseForm(0, 0, 0))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").exists())
                .andExpect(cookie().exists(RefreshProvider.COOKIE_NAME));
        assertEquals(1, sessionRepository.count());
        ArrayList<String> invalidCases = new ArrayList<>() {{
            add(parseForm(1, 0, 0));
            add(parseForm(0, 1, 0));
            add(parseForm(0, 0, 1));
            add(parseForm(2, 0, 0));
            add(parseForm(0, 2, 0));
            add(parseForm(0, 0, 2));
        }};
        ArrayList<ResultMatcher> expectations = new ArrayList<>() {{
            add(status().isUnprocessableEntity());
            add(status().isUnprocessableEntity());
            add(status().isUnprocessableEntity());
            add(status().isNotFound());
            add(status().isUnauthorized());
            add(status().isOk());
        }};
        for (int i = 0; i < invalidCases.size(); i++) {
            System.out.println("CASE " + i);
            mvc.perform(MockMvcRequestBuilders
                    .post("/authorisation/login")
                    .content(invalidCases.get(i))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(expectations.get(i));
        }
        assertEquals(2, sessionRepository.count());
    }

    @Test
    void logout() throws Exception {
        assertEquals(1, sessionRepository.count());
        mvc.perform(MockMvcRequestBuilders
                .post("/authorisation/logout")
                .content(parseForm(0, 0, 0))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .cookie(new Cookie(RefreshProvider.COOKIE_NAME, refresh)))
                .andExpect(status().isNoContent());
        assertEquals(0, sessionRepository.count());
    }

    @Test
    void refresh() throws Exception {
        assertEquals(1, sessionRepository.count());
        String refresh = sessionRepository.findByFingerprint(fingerprint).get().getRefresh();
        mvc.perform(MockMvcRequestBuilders
                .post("/authorisation/refresh")
                .content(parseForm(3, 3, 0))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .cookie(new Cookie(RefreshProvider.COOKIE_NAME, refresh)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.jwt").exists())
                .andExpect(cookie().exists(RefreshProvider.COOKIE_NAME));
        assertEquals(1, sessionRepository.count());
        assertNotEquals(refresh, sessionRepository.findByFingerprint(fingerprint).get().getRefresh());
    }

    String parseForm(int emailType, int pswType, int fingerprintType) {
        User user = new User();
        user.setEmail(switch (emailType) {
            case 0 -> email;
            case 1 -> invalidEmail;
            case 2 -> anotherEmail;
            case 3 -> null;
            default -> throw new IllegalStateException("Unexpected value: " + emailType);
        });
        user.setPsw(switch (pswType) {
            case 0 -> psw;
            case 1 -> invalidPsw;
            case 2 -> anotherPsw;
            case 3 -> null;
            default -> throw new IllegalStateException("Unexpected value: " + pswType);
        });
        AuthForm authForm = new AuthForm();
        authForm.setUser(user);
        authForm.setFingerprint(switch (fingerprintType) {
            case 0 -> fingerprint;
            case 1 -> invalidFingerprint;
            case 2 -> anotherFingerprint;
            case 3 -> null;
            default -> throw new IllegalStateException("Unexpected value: " + fingerprintType);
        });
        return parser.toJson(authForm, AuthForm.class);
    }
}