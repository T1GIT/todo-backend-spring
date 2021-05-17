package com.todo.app.security.token;

import com.todo.app.api.util.CookieUtil;
import com.todo.app.security.crypt.KeyGenerator;
import com.todo.app.security.util.enums.KeyLength;
import com.todo.app.security.util.exception.MissedRefreshException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Optional;


public abstract class RefreshProvider {

    public final static Duration DURATION = Duration.ofDays(90);
    public final static String COOKIE_NAME = KeyGenerator.string(KeyLength.REFRESH_NAME);

    public static void attach(HttpServletResponse response, String refresh) {
        response.addCookie(
                new Cookie(COOKIE_NAME, refresh) {{
                    setHttpOnly(true);
                    setMaxAge((int) DURATION.getSeconds());
                }});
    }

    public static String extract(HttpServletRequest request) throws MissedRefreshException {
        return Optional.ofNullable(
                CookieUtil.get(request, COOKIE_NAME)
        ).orElseThrow(MissedRefreshException::new);
    }

    public static void erase(HttpServletResponse response) {
        CookieUtil.remove(response, COOKIE_NAME);
    }
}
