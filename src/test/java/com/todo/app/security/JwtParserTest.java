package com.todo.app.security;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class JwtParserTest {

    static Auth user = Auth.fromMap(new HashMap<>() {{
        put("id", Integer.MAX_VALUE + 1);
        put("email", "example@mail.ru");
        put("name", "name");
        put("surname", "surname");
        put("patronymic", "patronymic");
        put("birthdate", new Date());
    }});

    @Test
    void stringify() {
        System.out.println(JwtParser.stringify(user));
    }

    @Test
    void parse() {
        Auth parsedUser = JwtParser.parse(JwtParser.stringify(user));
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
        String jwt = JwtParser.stringify(user);
        assertTimeout(Duration.ofSeconds(seconds), () -> {
            for (int i = 0; i < amount; i++)
                JwtParser.parse(jwt);
        });
        assertTimeout(Duration.ofSeconds(seconds), () -> {
            for (int i = 0; i < amount; i++)
                JwtParser.stringify(user);
        });
    }
}