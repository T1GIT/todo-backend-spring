package com.todo.app.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

        assertFalse(Validator.psw(""));
        assertFalse(Validator.psw(1 + "a".repeat(120)));
        assertFalse(Validator.psw("password@"));
        assertFalse(Validator.psw("passwo1"));
        assertFalse(Validator.psw("passworddddd"));
        assertFalse(Validator.psw("111111111111"));
        assertFalse(Validator.psw("@@@@@@@@@@@@"));
    }
}