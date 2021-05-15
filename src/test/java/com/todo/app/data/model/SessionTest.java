package com.todo.app.data.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class SessionTest {

    static Session session = new Session() {{
        setRefresh("value");
    }};

    static Session sameSession = new Session() {{
        setRefresh("value");
    }};

    static Session anotherSession = new Session() {{
        setRefresh("another value");
    }};

    @Test
    void equalsTest() {
        assertEquals(session, sameSession);
        assertNotEquals(session, anotherSession);
    }

    @Test
    void toStringTest() {
        System.out.println(session);
        System.out.println(sameSession);
        System.out.println(anotherSession);
    }

}