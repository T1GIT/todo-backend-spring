package com.todo.app.api.controller;

import com.todo.app.api.util.exception.IncorrectEmailException;
import com.todo.app.api.util.exception.IncorrectFingerprintException;
import com.todo.app.api.util.exception.IncorrectPswException;
import com.todo.app.api.util.json.request.AuthForm;
import com.todo.app.api.util.json.response.JwtJson;
import com.todo.app.data.model.Session;
import com.todo.app.data.model.User;
import com.todo.app.data.service.SessionService;
import com.todo.app.data.service.UserService;
import com.todo.app.security.Validator;
import com.todo.app.security.token.JwtProvider;
import com.todo.app.security.token.RefreshProvider;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Api(tags = "Authorisation controller",
        description = "Controller to provide authorisation process")
@RequiredArgsConstructor
@RestController
@RequestMapping("/authorisation")
public class AuthorisationController {

    private final UserService userService;
    private final SessionService sessionService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public JwtJson register(
            @RequestBody AuthForm authForm,
            HttpServletResponse response) {
        validateAuthForm(authForm);
        System.out.println(authForm.getUser());
        User user = userService.register(authForm.getUser());
        return createSession(user, authForm.getFingerprint(), response);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public JwtJson login(
            @RequestBody AuthForm authForm,
            HttpServletResponse response) {
        validateAuthForm(authForm);
        User user = userService.login(authForm.getUser());
        return createSession(user, authForm.getFingerprint(), response);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping( "/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        sessionService.delete(RefreshProvider.extract(request));
        RefreshProvider.erase(response);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/refresh")
    public JwtJson refresh(
            @RequestBody Session session,
            HttpServletRequest request, HttpServletResponse response) {
        return updateSession(session.getFingerprint(), request, response);
    }

    private void validateAuthForm(AuthForm form) {
        User user = form.getUser();
        String fingerprint = form.getFingerprint();
        if (!Validator.email(user.getEmail()))
            throw new IncorrectEmailException(user.getEmail());
        if (!Validator.psw(user.getPsw()))
            throw new IncorrectPswException(user.getPsw());
        if (!Validator.fingerprint(fingerprint))
            throw new IncorrectFingerprintException(fingerprint);
    }

    private JwtJson createSession(User user, String fingerprint, HttpServletResponse response) {
        Session session = sessionService.create(user.getId(), fingerprint);
        RefreshProvider.attach(response, session.getRefresh());
        return new JwtJson() {{
            setJwt(JwtProvider.getJwt(user));
        }};
    }

    private JwtJson updateSession(String fingerprint, HttpServletRequest request, HttpServletResponse response) {
        String refresh = RefreshProvider.extract(request);
        Session session = sessionService.update(refresh, fingerprint);
        RefreshProvider.attach(response, session.getRefresh());
        return new JwtJson() {{
            setJwt(JwtProvider.getJwt(session.getUser()));
        }};
    }
}
