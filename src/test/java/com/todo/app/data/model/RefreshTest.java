package com.todo.app.data.model;

import com.todo.app.security.util.enums.Role;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class RefreshTest {

    static Refresh refresh = new Refresh() {{
        setValue("value");
    }};

    static Refresh sameRefresh = new Refresh() {{
        setValue("value");
    }};

    static Refresh anotherRefresh = new Refresh() {{
        setValue("another value");
    }};

    @Test
    void equalsTest() {
        assertEquals(refresh, sameRefresh);
        assertNotEquals(refresh, anotherRefresh);
    }

    @Test
    void toStringTest() {
        System.out.println(refresh);
        System.out.println(sameRefresh);
        System.out.println(anotherRefresh);
    }

}