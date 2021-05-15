package com.todo.app.security.token;

import com.todo.app.api.util.CookieUtil;
import com.todo.app.security.util.exception.MissedRefreshException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;


public abstract class RefreshProvider {

    private final static Duration DURATION = Duration.ofDays(30);

    public static void attach(HttpServletResponse response, String refresh) {
        response.addCookie(
                new Cookie("REFRESH", refresh) {{
                    setHttpOnly(true);
                    setMaxAge((int) DURATION.getSeconds());
                }});
    }

    public static String extract(HttpServletRequest request) throws MissedRefreshException {
        return Optional.ofNullable(
                CookieUtil.get(request, "REFRESH")
        ).orElseThrow(MissedRefreshException::new);
    }

    public static void erase(HttpServletResponse response) {
        CookieUtil.remove(response, "REFRESH");
    }
}
