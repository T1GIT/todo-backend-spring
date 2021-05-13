package com.todo.app.security.token;

import com.google.common.base.Optional;
import com.todo.app.api.util.CookieUtil;
import com.todo.app.data.model.Refresh;
import com.todo.app.security.Auth;
import com.todo.app.security.KeyGenerator;
import com.todo.app.security.util.enums.SecretLength;
import com.todo.app.security.util.exception.MissedJwtException;
import com.todo.app.security.util.exception.MissedRefreshException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public abstract class RefreshProvider {

    private final static Duration DURATION = Duration.ofDays(30);
    private final static String TOKEN_NAME = "REFRESH";

    public static void attach(HttpServletResponse response, String refresh) {
        CookieUtil.add(response, TOKEN_NAME, refresh, DURATION.getSeconds());
    }

    public static String extract(HttpServletRequest request) throws MissedRefreshException {
        String refresh = CookieUtil.get(request, TOKEN_NAME);
        if (refresh == null)
            throw new MissedRefreshException();
        return refresh;
    }

    public static void erase(HttpServletResponse response) {
        CookieUtil.add(response, TOKEN_NAME, null, 0);
    }
}
