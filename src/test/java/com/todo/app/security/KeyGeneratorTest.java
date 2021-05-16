package com.todo.app.security;

import com.todo.app.security.crypt.KeyGenerator;
import com.todo.app.security.util.enums.KeyLength;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeyGeneratorTest {

    @Test
    void string() {
        KeyLength keyLength = KeyLength.REFRESH;
        int length = keyLength.getLength();
        assertEquals(length, KeyGenerator.string(keyLength).length());
        String example = KeyGenerator.string(keyLength);
        for (int i = 0; i < length; i++) {
            String test = KeyGenerator.string(keyLength);
            assertNotEquals(example, test);
            example = test;
        }
    }

    @Test
    void bytes() {
        KeyLength keyLength = KeyLength.SALT;
        int length = keyLength.getLength();
        assertEquals(length, KeyGenerator.bytes(keyLength).length);
        byte[] example = KeyGenerator.bytes(keyLength);
        for (int i = 0; i < length; i++) {
            byte[] test = KeyGenerator.bytes(keyLength);
            assertNotEquals(example, test);
            example = test;
        }
    }
}