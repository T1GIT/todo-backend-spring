package com.todo.app.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidatorTest {

    @Test
    void email() {
        assertTrue(Validator.email("example@mail.ru"));
        assertTrue(Validator.email("EXAMPLE@MAIL.RU"));
        assertTrue(Validator.email("example1@mail.ru"));
        assertTrue(Validator.email("example@mail1.ru"));
        assertTrue(Validator.email("ex-ample@mail.ru"));
        assertTrue(Validator.email("example@ma-il.ru"));
        assertTrue(Validator.email("ex_ample@mail.ru"));
        assertTrue(Validator.email("example@ma.il.ru"));
        assertTrue(Validator.email("example@mail.ru"));
        assertTrue(Validator.email("example@mail.ru"));
        assertTrue(Validator.email("example@mail.ru"));
        assertTrue(Validator.email("example@mail.ru"));
        assertTrue(Validator.email("example@mail.ru"));
        assertTrue(Validator.email("example@mail.ru"));

        assertFalse(Validator.email(null));
        assertFalse(Validator.email(""));
        assertFalse(Validator.email("example@mailru"));
        assertFalse(Validator.email("e".repeat(300) + "example@mail.ru"));
        assertFalse(Validator.email("examplemail.ru"));
        assertFalse(Validator.email("ex ample@mail.ru"));
        assertFalse(Validator.email("example@mail. ru"));
        assertFalse(Validator.email("@mail.ru"));
        assertFalse(Validator.email("example@mail"));
        assertFalse(Validator.email("example@mail.ru" + "u".repeat(70)));
        assertFalse(Validator.email("example@mail.r"));
    }

    @Test
    void psw() {
        assertTrue(Validator.psw("password1"));
        assertTrue(Validator.psw("passwor1"));
        assertTrue(Validator.psw("password1("));
        assertTrue(Validator.psw("password1!@#$%^&*()_+-=\\|/"));
        assertTrue(Validator.psw("password1"));
        assertTrue(Validator.psw("1" + "a".repeat(119)));

        assertFalse(Validator.psw(null));
        assertFalse(Validator.psw(""));
        assertFalse(Validator.psw(1 + "a".repeat(120)));
        assertFalse(Validator.psw("password@"));
        assertFalse(Validator.psw("passwo1"));
        assertFalse(Validator.psw("passworddddd"));
        assertFalse(Validator.psw("111111111111"));
        assertFalse(Validator.psw("@@@@@@@@@@@@"));
    }

    @Test
    void fingerprint() {
        assertTrue(Validator.fingerprint("a".repeat(20)));
        assertTrue(Validator.fingerprint("a".repeat(10)));
        assertTrue(Validator.fingerprint("a".repeat(50)));
        assertTrue(Validator.fingerprint("a1".repeat(10)));
        assertTrue(Validator.fingerprint("a1B".repeat(10)));

        assertFalse(Validator.fingerprint(null));
        assertFalse(Validator.fingerprint("a".repeat(100)));
        assertFalse(Validator.fingerprint("a".repeat(9)));
        assertFalse(Validator.fingerprint("a".repeat(51)));
        assertFalse(Validator.fingerprint("a-".repeat(10)));
        assertFalse(Validator.fingerprint("aФ".repeat(10)));
    }
}