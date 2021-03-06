package com.todo.app.api.util;

import com.todo.app.TodoApplication;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * Provides static methods for working with cookies.
 */
public abstract class CookieUtil {

    private static final Charset ENCODING = StandardCharsets.UTF_8;

    private static String encode(String row) {
        return URLEncoder.encode(row, ENCODING);
    }

    private static String decode(String row) {
        return URLEncoder.encode(row, ENCODING);
    }

    /**
     * Checks if cookie with the given name in the request.
     *
     * @param request for searching cookie
     * @param name    cookie name
     * @return value of the cookie or null if cookie was not found
     */
    public static boolean exists(HttpServletRequest request, String name) {
        return Objects.requireNonNullElse(Arrays
                        .stream(request.getCookies())
                        .anyMatch(c -> c.getName().equals(encode(name))),
                null);
    }

    /**
     * Gets a cookie from the httpServletRequest by its name.
     *
     * @param request for searching cookie
     * @param name    cookie name
     * @return value of the cookie or null if cookie was not found
     */
    public static String get(HttpServletRequest request, String name) {
        return Optional.ofNullable(request.getCookies()).flatMap(
                cookies -> Arrays.stream(cookies)
                        .filter(c -> c.getName().equals(name)).findAny()
                        .map(c -> decode(c.getValue()))
        ).orElse(null);
    }

    /**
     * Creates, configures and adds cookie object to the response.
     *
     * @param response  target to add cookie
     * @param name      of the cookie
     * @param value     of the cookie
     * @param expiresIn life time of the cookie in seconds
     */
    public static void add(HttpServletResponse response, String name, String value, long expiresIn) {
        response.addCookie(
                new Cookie(encode(name), encode(value)) {{
                    setHttpOnly(true);
                    setMaxAge((int) expiresIn);
                    setDomain(TodoApplication.DOMAIN);
                }});
    }

    /**
     * Creates, configures and adds cookie object to the response.
     * Gives to it maximum expiring time.
     *
     * @param response target to add cookie
     * @param name     of the cookie
     * @param value    of the cookie
     */
    public static void add(HttpServletResponse response, String name, String value) {
        add(response, name, value, Integer.MAX_VALUE);
    }

    /**
     * Removes cookie in the response.
     *
     * @param response to remove
     * @param name     of the cookie to delete
     */
    public static void remove(HttpServletResponse response, String name) {
        add(response, name, "", 0);
    }
}
