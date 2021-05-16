package com.todo.app.api.controller;

import com.todo.app.api.util.exception.InvalidEmailException;
import com.todo.app.api.util.exception.InvalidPswException;
import com.todo.app.api.util.json.AuthForm;
import com.todo.app.api.util.json.request.FingerprintJson;
import com.todo.app.api.util.json.request.LoginFormJson;
import com.todo.app.api.util.json.request.RegisterFormJson;
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
            @RequestBody AuthForm form,
            HttpServletResponse response) {
        User user = form.getUser();
        String fingerprint = form.getFingerprint();
        if (!Validator.email(user.getEmail()))
            throw new InvalidEmailException(user.getEmail());
        if (!Validator.psw(user.getPsw()))
            throw new InvalidPswException(user.getPsw());
        return createSession(userService.register(user), fingerprint, response);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public JwtJson login(
            @RequestBody LoginFormJson loginForm,
            HttpServletResponse response) {
        if (!Validator.email(loginForm.getEmail()))
            throw new InvalidEmailException(loginForm.getEmail());
        if (!Validator.psw(loginForm.getPsw()))
            throw new InvalidPswException(loginForm.getPsw());
        return createSession(userService.login(loginForm), loginForm.getFingerprint(), response);
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
            @RequestBody FingerprintJson fingerprintJson,
            HttpServletRequest request, HttpServletResponse response) {
        return updateSession(fingerprintJson.getFingerprint(), request, response);
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
