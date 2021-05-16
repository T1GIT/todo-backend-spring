package com.todo.app.security.crypt;

import java.math.BigInteger;


/**
 * Provides static methods for converting between
 * bytes array and hex string representation
 */
public abstract class HexConvertor {

    /**
     * Converts bytes array into a hex string
     *
     * @param array bytes array for converting
     * @return converted hex string
     */
    public static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = array.length * 2 - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    /**
     * Converts hex string into a bytes array
     *
     * @param hex string for converting
     * @return converted bytes array
     */
    public static byte[] fromHex(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

}
