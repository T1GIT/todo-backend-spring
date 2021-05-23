package com.todo.app.api.controller.impl;

import com.todo.app.api.controller.AuthorisationController;
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
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RequiredArgsConstructor
@RestController
public class AuthorisationControllerImpl implements AuthorisationController {

    private final UserService userService;
    private final SessionService sessionService;

    @Override
    public JwtJson register(AuthForm form, HttpServletResponse response) {
        validateAuthForm(form);
        User user = userService.register(form.getUser());
        return createSession(user, form.getFingerprint(), response);
    }

    @Override
    public JwtJson login(AuthForm form, HttpServletResponse response) {
        validateAuthForm(form);
        User user = userService.login(form.getUser());
        return createSession(user, form.getFingerprint(), response);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        RefreshProvider.erase(response);
        sessionService.delete(RefreshProvider.extract(request));
    }

    @Override
    public JwtJson refresh(AuthForm form, HttpServletRequest request, HttpServletResponse response) {
        return updateSession(form.getFingerprint(), request, response);
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
