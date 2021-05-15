package com.todo.app.api.filter;

import com.todo.app.security.token.JwtProvider;
import com.todo.app.security.util.exception.MissedJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            SecurityContextHolder.getContext().setAuthentication(
                    JwtProvider.extract(request));
        } catch (JwtException | MissedJwtException ignored) {}
        filterChain.doFilter(request, response);
    }
}
