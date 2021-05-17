package com.todo.app.security.token;

import com.todo.app.TodoApplication;
import com.todo.app.data.model.User;
import com.todo.app.security.auth.AuthUser;
import com.todo.app.security.crypt.KeyGenerator;
import com.todo.app.security.util.enums.KeyLength;
import com.todo.app.security.util.exception.MissedJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.time.Duration;
import java.util.Date;


public abstract class JwtProvider {

    public final static Duration DURATION = Duration.ofHours(1);

    public static Key KEY = Keys.hmacShaKeyFor(KeyGenerator.bytes(KeyLength.JWT_KEY));

    public static String getJwt(User user) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setExpiration(new Date(System.currentTimeMillis() + DURATION.toMillis()))
                .setIssuedAt(new Date())
                .setIssuer(TodoApplication.ADDRESS)
                .setAudience(user.getRole().name())
                .setClaims(AuthUser.toMap(user))
                .signWith(KEY)
                .compact();
    }

    public static AuthUser parseUser(String jwt) throws JwtException {
        return AuthUser.fromMap(
                Jwts.parserBuilder()
                        .setSigningKey(KEY)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody());
    }

    public static AuthUser extract(HttpServletRequest request) throws JwtException, MissedJwtException {
        String jwt = request.getHeader("authorization");
        if (jwt == null)
            throw new MissedJwtException();
        return parseUser(jwt.substring(7));
    }

    public static void updateKey() {
        KEY = Keys.hmacShaKeyFor(KeyGenerator.bytes(KeyLength.JWT_KEY));
    }
}
