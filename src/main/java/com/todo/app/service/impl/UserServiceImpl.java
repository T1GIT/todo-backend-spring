package com.todo.app.service.impl;

import com.todo.app.model.User;
import com.todo.app.repo.UserRepository;
import com.todo.app.service.UserService;
import com.todo.app.utils.abstractService.impl.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional
public class UserServiceImpl
        extends AbstractService<User, UserRepository>
        implements UserService {

    public UserServiceImpl(UserRepository repository) {
        super(repository);
    }

    @Override
    public Optional<User> getByLogin(String login) {
        return repository.findUserByLogin(login);
    }
}
