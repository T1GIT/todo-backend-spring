package com.todo.app.api.controller.impl;

import com.todo.app.api.controller.UserController;
import com.todo.app.api.util.exception.IncorrectEmailException;
import com.todo.app.api.util.exception.IncorrectPswException;
import com.todo.app.data.model.User;
import com.todo.app.data.service.UserService;
import com.todo.app.security.Validator;
import com.todo.app.security.auth.AuthContext;
import com.todo.app.security.token.RefreshProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;


@RequiredArgsConstructor
@RestController
public class UserControllerImpl implements UserController {

    private final UserService userService;
    private final AuthContext authContext;

    @Override
    public void changeEmail(User user) {
        if (!Validator.email(user.getEmail()))
            throw new IncorrectEmailException(user.getEmail());
        userService.changeEmail(authContext.getUser().getId(), user.getEmail());
    }

    @Override
    public void changePsw(User user) {
        if (!Validator.psw(user.getPsw()))
            throw new IncorrectPswException(user.getPsw());
        userService.changePsw(authContext.getUser().getId(), user.getPsw());
    }

    @Override
    public void updateUser(User user) {
        userService.update(authContext.getUser().getId(), user);
    }

    @Override
    public void deleteUser(HttpServletResponse response) {
        userService.delete(authContext.getUser().getId());
        RefreshProvider.erase(response);
    }
}
