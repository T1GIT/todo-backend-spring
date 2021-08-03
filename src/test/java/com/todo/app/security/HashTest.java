package com.todo.app.security;

import com.todo.app.security.crypt.Hash;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static java.time.Duration.*;
import static org.junit.jupiter.api.Assertions.*;

class HashTest {

    static String psw = "some password";

    @Test
    void crypt() {
        String example = Hash.hash(psw);
        int amount = 10;
        for (int i = 0; i < amount; i++) {
            assertNotEquals(example, Hash.hash(example + i));
        }
    }

    @Test
    void check() {
        int amount = 5;
        for (int i = 0; i < amount; i++) {
            String psw = HashTest.psw + i;
            String hash = Hash.hash(psw);
            String invalidHash = Hash.hash("invalid_" + psw);
            assertTrue(Hash.check(psw, hash));
            assertFalse(Hash.check(psw, invalidHash));
        }
    }

    @Test
    void speedTest() {
        int min = 100;
        int max = 2000;
        String hash = Hash.hash(psw);
        String invalidHash = Hash.hash("invalid_" + psw);
        assertThrows(
                AssertionFailedError.class,
                () -> assertTimeout(
                        ofMillis(min),
                        () -> Hash.check(psw, hash)));
        assertThrows(
                AssertionFailedError.class,
                () -> assertTimeout(
                        ofMillis(min),
                        () -> Hash.check(psw, invalidHash)));
        assertTimeout(
                ofMillis(max),
                () -> Hash.check(psw, hash));
        assertTimeout(
                ofMillis(max),
                () -> Hash.check(psw, invalidHash));
    }
}