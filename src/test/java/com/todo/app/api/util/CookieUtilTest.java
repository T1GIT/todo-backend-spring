package com.todo.app.api.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.Cookie;

import static org.junit.jupiter.api.Assertions.*;

class CookieUtilTest {

    static MockHttpServletRequest request;
    static MockHttpServletResponse response;

    static String name = "cookie_name";
    static String value = "cookie_value";
    static int amount = 10;

    @BeforeEach
    void beforeEach() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void exists() {
        addCookies(request, amount);
        for (int i = 0; i < amount; i++) {
            assertTrue(CookieUtil.exists(request, name + i));
        }
        for (int i = 0; i < amount; i++) {
            assertFalse(CookieUtil.exists(request, name + (amount + i)));
        }
    }

    @Test
    void get() {
        addCookies(request, amount);
        for (int i = 0; i < amount; i++) {
            assertNotNull(CookieUtil.get(request, name + i));
        }
        for (int i = 0; i < amount; i++) {
            assertNull(CookieUtil.get(request, name + (amount + i)));
        }
        assertNull(CookieUtil.get(new MockHttpServletRequest(), name));
    }

    @Test
    void add() {
        for (int i = 0; i < amount; i++) {
            CookieUtil.add(response, name + i, value + i);
        }
        for (int i = 0; i < amount; i++) {
            CookieUtil.add(response, name + (amount + i), value + (amount + i), 100);
        }
        Cookie[] cookies = response.getCookies();
        assertEquals(amount * 2, cookies.length);
        for (int i = 0; i < amount * 2; i++) {
            assertEquals(value + i, cookies[i].getValue());
        }
    }

    @Test
    void remove() {
        for (int i = 0; i < amount; i++) {
            CookieUtil.add(response, name + i, value + i);
        }
        for (int i = 0; i < amount; i++) {
            CookieUtil.remove(response, name + i);
        }
        Cookie[] cookies = response.getCookies();
        assertEquals(amount * 2, cookies.length);
        for (int i = 0; i < amount; i++) {
            Cookie cookie = cookies[amount + i];
            assertTrue(cookie.getName().equals(name + i) && cookie.getMaxAge() == 0);
        }
    }

    void addCookies(MockHttpServletRequest request, int amount) {
        Cookie[] cookies = new Cookie[amount];
        for (int i = 0; i < amount; i++) {
            cookies[i] = new Cookie(name + i, value + i);
        }
        request.setCookies(cookies);
    }
}