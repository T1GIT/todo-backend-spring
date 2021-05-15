package com.todo.app.api.filter;

import com.todo.app.data.model.Refresh;
import com.todo.app.data.service.RefreshService;
import com.todo.app.data.util.exception.ResourceNotFoundException;
import com.todo.app.security.auth.AuthUser;
import com.todo.app.security.token.JwtProvider;
import com.todo.app.security.token.RefreshProvider;
import com.todo.app.security.util.exception.MissedJwtException;
import com.todo.app.security.util.exception.MissedRefreshException;
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
