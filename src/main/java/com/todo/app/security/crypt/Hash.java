package com.todo.app.security.crypt;

import com.todo.app.security.util.enums.KeyLength;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


/**
 * Provides static methods for hashing passwords.
 * <p>
 * Uses algorithms:
 * <ol>
 * <li> SHA1PRNG for generating salt
 * <li> PBKDF2WithHmac for generating random keys
 * <li> SHA512 for hashing
 * </ol>
 */
public abstract class Hash {

    /**
     * Amount of hash algorithm repeating
     */
    private static final int ITERATIONS = 100000;

    /**
     * Amount of symbols in the key for the algorithm.
     */
    private static final int KEY_LENGTH = 512;

    /**
     * Name of the used algorithm.
     */
    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";

    /**
     * Parses hash string from the password string.
     * Uses PBKDF2WithHmac algorithm for generating key
     * and SHA512 for hashing.
     *
     * @param password string to hash
     * @return String encrypted string with template:
     * [algorithm]:[iterations]:[salt]:[hash]
     */
    public static String hash(String password) {
        char[] chars = password.toCharArray();
        byte[] salt = KeyGenerator.bytes(KeyLength.SALT);
        byte[] hash = null;
        try {
            PBEKeySpec spec = new PBEKeySpec(chars, salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
            hash = skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return String.format("%s:%d:%s:%s", ALGORITHM, ITERATIONS, HexConvertor.toHex(salt), HexConvertor.toHex(hash));
    }

    /**
     * Checks if password matches hash.
     *
     * @param psw     raw password
     * @param pswHash password hash
     * @return true if passwords matches
     */
    public static boolean check(String psw, String pswHash) {
        try {
            String[] parts = pswHash.split(":");
            String algorithm = parts[0];
            int iterations = Integer.parseInt(parts[1]);
            byte[] salt = HexConvertor.fromHex(parts[2]);
            byte[] hash = HexConvertor.fromHex(parts[3]);

            PBEKeySpec spec = new PBEKeySpec(psw.toCharArray(), salt, iterations, hash.length * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithm);
            byte[] testHash = skf.generateSecret(spec).getEncoded();

            int diff = hash.length ^ testHash.length;
            for (int i = 0; i < hash.length && i < testHash.length; i++) {
                diff |= hash[i] ^ testHash[i];
            }
            return diff == 0;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return false;
        }
    }
}
