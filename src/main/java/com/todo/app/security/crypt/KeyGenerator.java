package com.todo.app.security.crypt;

import com.todo.app.security.util.enums.KeyLength;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;


public abstract class KeyGenerator {

    private static final String ALGORITHM = "SHA1PRNG";
    private static final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
    private static SecureRandom secureRandom;

    static {
        try {
            secureRandom = SecureRandom.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String string(int keyLength) {
        return encoder.encodeToString(bytes(keyLength)).substring(0, keyLength);
    }

    public static String string(KeyLength keyLength) {
        return string(keyLength.getLength());
    }

    public static byte[] bytes(int keyLength) {
        byte[] bytes = new byte[keyLength];
        secureRandom.nextBytes(bytes);
        return bytes;
    }

    public static byte[] bytes(KeyLength keyLength) {
        return bytes(keyLength.getLength());
    }
}
