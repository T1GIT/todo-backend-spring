package com.todo.app.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashTest {

    static String psw = "some password";

    @Test
    void crypt() {
        String example = Hash.encrypt(psw);
        int amount = 10;
        for (int i = 0; i < amount; i++) {
            assertNotEquals(example, Hash.encrypt(example + i));
        }
    }

    @Test
    void check() {
        int amount = 10;
        for (int i = 0; i < amount; i++) {
            String test = psw + i;
            String invalidTest = i + test.substring(1);
            assertTrue(Hash.check(test, Hash.encrypt(test)));
            assertFalse(Hash.check(psw, Hash.encrypt(invalidTest)));
        }
    }
}