package com.todo.app.api.config;

import com.todo.app.api.filter.SecurityFilter;
import com.todo.app.security.util.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SecurityFilter securityFilter;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .httpBasic().disable()
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(
                        "swagger-ui.html",
                        "/docs/**",
                        "/webjars/**",
                        "/authorisation/**"
                ).permitAll()
                .antMatchers(
                        "/todo/**",
                        "/user/**"
                ).authenticated()
                .antMatchers(
                        "/admin/**"
                ).hasRole(Role.ADMIN.name())
                .and()
                .addFilterAfter(securityFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
