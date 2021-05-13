package com.todo.app.security;

import com.todo.app.security.auth.AuthUser;
import com.todo.app.security.token.JwtProvider;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class JwtProviderTest {

    static AuthUser user = AuthUser.fromMap(new HashMap<>() {{
        put("id", Integer.MAX_VALUE);
        put("email", "example@mail.ru");
        put("name", "name");
        put("surname", "surname");
        put("patronymic", "patronymic");
        put("birthdate", new Date());
    }});

    @Test
    void stringify() {
        System.out.println(JwtProvider.stringify(user));
    }

    @Test
    void parse() {
        AuthUser parsedUser = JwtProvider.parse(JwtProvider.stringify(user));
        assertEquals(user.getId(), parsedUser.getId());
        assertEquals(user.getName(), parsedUser.getName());
        assertEquals(user.getSurname(), parsedUser.getSurname());
        assertEquals(user.getPatronymic(), parsedUser.getPatronymic());
        assertEquals(user.getBirthdate(), parsedUser.getBirthdate());
    }

    @Test
    void speedTest() {
        int amount = 50;
        int seconds = 1;
        String jwt = JwtProvider.stringify(user);
        assertTimeout(Duration.ofSeconds(seconds), () -> {
            for (int i = 0; i < amount; i++)
                JwtProvider.parse(jwt);
        });
        assertTimeout(Duration.ofSeconds(seconds), () -> {
            for (int i = 0; i < amount; i++)
                JwtProvider.stringify(user);
        });
    }
}