package com.todo.app.security;

import com.todo.app.security.util.enums.SecretLength;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import java.security.Key;
import java.time.Duration;
import java.util.Date;


/**
 * Provides static methods for parsing and creating JWTs.
 */
public abstract class JwtParser {

    /**
     * Expires duration of the JWT cookie
     */
    private final static Duration DURATION = Duration.ofHours(1);

    /**
     * Hmac SHA key, parsed from the key, produce by the {@link KeyGenerator}
     */
    private final static Key KEY = Keys.hmacShaKeyFor(KeyGenerator.bytes(SecretLength.JWT_KEY));

    /**
     * Creates the JWT from the user object.
     *
     * @param user target to parse
     * @return parsed JWT
     */
    public static String stringify(Auth user) {
        return Jwts.builder()
                .setClaims(Auth.toMap(user))
                .setExpiration(new Date(System.currentTimeMillis() + DURATION.toMillis()))
                .signWith(KEY)
                .compact();
    }

    /**
     * Parses {@link Auth} object from the JWT
     *
     * @param jwt input JWT
     * @return parsed user
     * @throws SignatureException    if signature is invalid
     * @throws MalformedJwtException if JWT is broken
     */
    public static Auth parse(String jwt) throws SignatureException, MalformedJwtException {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
        return Auth.fromMap(claims);
    }
}
