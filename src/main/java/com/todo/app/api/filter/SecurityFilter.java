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
import javax.transaction.Transactional;
import java.io.IOException;


@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final RefreshService refreshService;

    public SecurityFilter(RefreshService refreshService) {
        this.refreshService = refreshService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String refreshCookie = RefreshProvider.extract(request);
            try {
                AuthUser user = JwtProvider.extract(request);
                SecurityContextHolder.getContext().setAuthentication(user);
            } catch (JwtException | MissedJwtException exception) {
                Refresh refresh = refreshService.update(refreshCookie);
                RefreshProvider.attach(response, refresh.getValue());
                JwtProvider.attach(response, refresh.getUser());
            }
        } catch (MissedRefreshException | ResourceNotFoundException e) {
            RefreshProvider.erase(response);
            JwtProvider.erase(response);
        }
        filterChain.doFilter(request, response);
    }
}
