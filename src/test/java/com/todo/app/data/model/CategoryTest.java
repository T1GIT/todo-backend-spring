package com.todo.app.data.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CategoryTest {

    static Category category = new Category() {{
        setName("name");
    }};

    static Category sameCategory = new Category() {{
        setName("name");
    }};

    static Category anotherCategory = new Category() {{
        setName("another name");
    }};

    @Test
    void equalsTest() {
        assertEquals(category, sameCategory);
        assertNotEquals(category, anotherCategory);
    }

    @Test
    void toStringTest() {
        System.out.println(category);
        System.out.println(sameCategory);
        System.out.println(anotherCategory);
    }

}