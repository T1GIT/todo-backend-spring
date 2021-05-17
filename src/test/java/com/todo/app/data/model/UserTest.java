package com.todo.app.data.model;

import com.todo.app.security.util.enums.Role;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class UserTest {

    static Date date = new Date(System.currentTimeMillis());

    static User user = new User() {{
        setEmail("example@mail.com");
        setPsw("password1");
        setName("Ivan");
        setSurname("Ivanov");
        setPatronymic("Ivanovich");
        setBirthdate(date);
        setRole(Role.BASIC);
    }};

    static User sameUser = new User() {{
        setEmail("example@mail.com");
        setPsw("password1");
        setName("Ivan");
        setSurname("Ivanov");
        setPatronymic("Ivanovich");
        setBirthdate(date);
        setRole(Role.BASIC);
    }};

    static User almostSameUser = new User() {{
        setEmail("example@mail.com");
        setPsw("password1");
        setName("Daniil");
        setSurname("Ivanov");
        setPatronymic("Ivanovich");
        setRole(Role.BASIC);
    }};

    static User anotherUser = new User() {{
        setEmail("example1@mail.com");
        setPsw("password2");
        setName("Petr");
        setSurname("Petrov");
        setPatronymic("Petrovich");
        setBirthdate(new Date());
        setRole(Role.ADMIN);
    }};

    @Test
    void equalsTest() {
        assertEquals(user, sameUser);
        assertNotEquals(user, almostSameUser);
        assertNotEquals(user, anotherUser);
    }

    @Test
    void toStringTest() {
        System.out.println(user);
        System.out.println(sameUser);
        System.out.println(almostSameUser);
        System.out.println(anotherUser);
    }
}