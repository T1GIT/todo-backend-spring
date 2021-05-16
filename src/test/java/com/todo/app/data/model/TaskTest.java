package com.todo.app.data.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TaskTest {

    static Task task = new Task() {{
        setTitle("title");
        setDescription("desc");
        setCompleted(true);
    }};

    static Task sameTask = new Task() {{
        setTitle("title");
        setDescription("desc");
        setCompleted(true);
    }};

    static Task almostSameTask = new Task() {{
        setTitle("title");
        setDescription("desc");
        setCompleted(false);
    }};

    static Task anotherTask = new Task() {{
        setTitle("another title");
        setDescription("another desc");
        setCompleted(false);
    }};

    @Test
    void equalsTest() {
        assertEquals(task, sameTask);
        assertNotEquals(task, almostSameTask);
        assertNotEquals(task, anotherTask);
    }

    @Test
    void toStringTest() {
        System.out.println(task);
        System.out.println(sameTask);
        System.out.println(almostSameTask);
        System.out.println(anotherTask);
    }

}