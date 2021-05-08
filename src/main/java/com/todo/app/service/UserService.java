package com.todo.app.service;


import com.todo.app.model.User;
import com.todo.app.utils.abstractService.ServiceInterface;

import java.util.Optional;

public interface UserService extends ServiceInterface<User> {
    Optional<User> getByLogin(String login);
}
