package com.todo.app.security.token;

import com.todo.app.api.util.CookieUtil;
import com.todo.app.data.model.User;
import com.todo.app.security.auth.AuthUser;
import com.todo.app.security.crypt.KeyGenerator;
import com.todo.app.security.util.enums.KeyLength;
import com.todo.app.security.util.exception.MissedJwtException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.time.Duration;
import java.util.Date;


public abstract class JwtProvider {

    private final static Duration DURATION = Duration.ofHours(1);
    private final static String TOKEN_NAME = "JWT";

    private final static Key KEY = Keys.hmacShaKeyFor(KeyGenerator.bytes(KeyLength.JWT_KEY));

    public static String stringify(User user) {
        return Jwts.builder()
                .setClaims(AuthUser.toMap(user))
                .setExpiration(new Date(System.currentTimeMillis() + DURATION.toMillis()))
                .signWith(KEY)
                .compact();
    }

    public static AuthUser parse(String jwt) throws JwtException {
        return AuthUser.fromMap(
                Jwts.parserBuilder()
                        .setSigningKey(KEY)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody());
    }

    public static void attach(HttpServletResponse response, User user) {
        CookieUtil.add(response, TOKEN_NAME, stringify(user), DURATION.getSeconds());
    }

    public static AuthUser extract(HttpServletRequest request) throws JwtException, MissedJwtException {
        String jwt = CookieUtil.get(request, TOKEN_NAME);
        if (jwt == null)
            throw new MissedJwtException();
        return parse(jwt);
    }

    public static void erase(HttpServletResponse response) {
        CookieUtil.add(response, TOKEN_NAME, null, 0);
    }
}
