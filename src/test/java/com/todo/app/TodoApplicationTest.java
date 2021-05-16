package com.todo.app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TodoApplicationTest {

    @Test
    void main() {
        assertDoesNotThrow(() -> TodoApplication.main(new String[0]));
    }
}