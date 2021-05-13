package com.todo.app.api.config;

import com.todo.app.api.filter.SecurityFilter;
import com.todo.app.security.util.enums.Role;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SecurityFilter securityFilter;

    public SecurityConfig(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(
                        "swagger-ui.html",
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