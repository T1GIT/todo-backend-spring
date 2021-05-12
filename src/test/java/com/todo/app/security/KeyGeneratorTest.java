package com.todo.app.security;

import com.todo.app.security.util.enums.SecretLength;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeyGeneratorTest {

    @Test
    void string() {
        SecretLength secretLength = SecretLength.SALT;
        int length = secretLength.getLength();
        assertEquals(length, KeyGenerator.string(secretLength).length());
        String example = KeyGenerator.string(secretLength);
        for (int i = 0; i < length; i++) {
            String test = KeyGenerator.string(secretLength);
            assertNotEquals(example, test);
            example = test;
        }
    }

    @Test
    void bytes() {
        SecretLength secretLength = SecretLength.SALT;
        int length = secretLength.getLength();
        assertEquals(length, KeyGenerator.bytes(secretLength).length);
        byte[] example = KeyGenerator.bytes(secretLength);
        for (int i = 0; i < length; i++) {
            byte[] test = KeyGenerator.bytes(secretLength);
            assertNotEquals(example, test);
            example = test;
        }
    }
}