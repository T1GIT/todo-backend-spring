package com.todo.app.security;

import com.todo.app.security.auth.AuthUser;
import com.todo.app.security.token.JwtProvider;
import com.todo.app.security.util.enums.Role;
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
        put("role", Role.BASIC.name());
    }});

    @Test
    void stringify() {
        String jwt = JwtProvider.getJwt(user);
        assertTrue(JwtProvider.getJwt(user).matches("^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*$"));
        assertTrue(jwt.length() < 500);
    }

    @Test
    void parse() {
        AuthUser parsedUser = JwtProvider.parseUser(JwtProvider.getJwt(user));
        assertEquals(user.getId(), parsedUser.getId());
        assertEquals(user.getName(), parsedUser.getName());
        assertEquals(user.getSurname(), parsedUser.getSurname());
        assertEquals(user.getPatronymic(), parsedUser.getPatronymic());
        assertEquals(user.getBirthdate(), parsedUser.getBirthdate());
        assertEquals(user.getRole(), parsedUser.getRole());
    }

    @Test
    void speedTest() {
        int amount = 50;
        int seconds = 1;
        String jwt = JwtProvider.getJwt(user);
        assertTimeout(Duration.ofSeconds(seconds), () -> {
            for (int i = 0; i < amount; i++)
                JwtProvider.parseUser(jwt);
        });
        assertTimeout(Duration.ofSeconds(seconds), () -> {
            for (int i = 0; i < amount; i++)
                JwtProvider.getJwt(user);
        });
    }
}