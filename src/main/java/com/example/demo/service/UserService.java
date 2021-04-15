package com.example.demo.service;


import com.example.demo.model.User;
import com.example.demo.utils.abstractService.ServiceInterface;

import java.util.Optional;

public interface UserService extends ServiceInterface<User> {
    Optional<User> getByLogin(String login);
}
