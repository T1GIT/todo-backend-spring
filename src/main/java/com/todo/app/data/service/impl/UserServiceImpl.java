package com.todo.app.data.service.impl;

import com.todo.app.data.exception.EmailExistsException;
import com.todo.app.data.exception.IncorrectPswException;
import com.todo.app.data.exception.ResourceNotFoundException;
import com.todo.app.data.exception.UserNotFoundException;
import com.todo.app.data.model.Category;
import com.todo.app.data.model.User;
import com.todo.app.data.repo.UserRepository;
import com.todo.app.data.service.UserService;
import com.todo.app.security.Hash;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.function.Consumer;


@Transactional
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User register(User user) throws EmailExistsException {
        if (userRepository.existsByEmail(user.getEmail()))
            throw new EmailExistsException(user.getEmail());
        return userRepository.saveAndFlush(user
                .edit(u -> {
                    u.setPsw(Hash.crypt(user.getPsw()));
                    u.addCategory(new Category().edit(
                            c -> c.setName("Задачи")));
                }));
    }

    @Override
    public User login(User user) throws UserNotFoundException, IncorrectPswException {
        return userRepository.saveAndFlush(
                userRepository.findByEmail(user.getEmail()).map(foundUser -> {
                    if (Hash.check(user.getPsw(), foundUser.getPsw())) {
                        return foundUser;
                    } else {
                        throw new IncorrectPswException(user.getEmail());
                    }
                }).orElseThrow(() -> new ResourceNotFoundException(User.class, "email", user.getEmail())));
    }

    @Override
    public void changeEmail(long userId, String newEmail) throws EmailExistsException, ResourceNotFoundException {
        if (userRepository.existsByEmail(newEmail))
            throw new EmailExistsException(newEmail);
        userRepository.saveAndFlush(
                userRepository.findById(userId).map(user -> user
                        .edit(u -> u.setEmail(newEmail))
                ).orElseThrow(() -> new ResourceNotFoundException(User.class, userId)));
    }

    @Override
    public void changePsw(long userId, String newPsw) throws ResourceNotFoundException {
        userRepository.saveAndFlush(
                userRepository.findById(userId).map(user -> user
                        .edit(u -> u.setPsw(Hash.crypt(newPsw)))
                ).orElseThrow(() -> new ResourceNotFoundException(User.class, userId))
        );
    }

    @Override
    public User edit(long userId, Consumer<User> editor) throws ResourceNotFoundException {
        return userRepository.saveAndFlush(
                userRepository.findById(userId).map(
                        user -> user.edit(editor)
                ).orElseThrow(() -> new ResourceNotFoundException(User.class, userId)));
    }

    @Override
    public void delete(long userId) {
        userRepository.delete(
                userRepository.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException(User.class, userId)));
    }
}
