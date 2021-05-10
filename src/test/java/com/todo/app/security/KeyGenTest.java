package com.todo.app.security;

import com.todo.app.security.util.enums.SecretLength;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class KeyGenTest {

    @Test
    void string() {
        SecretLength secretLength = SecretLength.SALT;
        int length = secretLength.getLength();
        assertEquals(length, KeyGen.string(secretLength).length());
        String example = KeyGen.string(secretLength);
        for (int i = 0; i < length; i++) {
            String test = KeyGen.string(secretLength);
            assertNotEquals(example, test);
            example = test;
        }
    }

    @Test
    void bytes() {
        SecretLength secretLength = SecretLength.SALT;
        int length = secretLength.getLength();
        assertEquals(length, KeyGen.bytes(secretLength).length);
        byte[] example = KeyGen.bytes(secretLength);
        for (int i = 0; i < length; i++) {
            byte[] test = KeyGen.bytes(secretLength);
            assertNotEquals(example, test);
            example = test;
        }
    }
}