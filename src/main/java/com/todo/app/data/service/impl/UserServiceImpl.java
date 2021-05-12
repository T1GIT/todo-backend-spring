package com.todo.app.data.service.impl;

import com.todo.app.data.util.exception.EmailExistsException;
import com.todo.app.data.util.exception.EmailNotExistsException;
import com.todo.app.data.util.exception.ResourceNotFoundException;
import com.todo.app.data.model.Category;
import com.todo.app.data.model.User;
import com.todo.app.data.repo.UserRepository;
import com.todo.app.data.service.UserService;
import com.todo.app.security.Hash;
import com.todo.app.security.util.exception.IncorrectPswException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


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
        return userRepository.saveAndFlush(
                user.edit(u -> {
                    u.setEmail(user.getEmail());
                    u.setPsw(Hash.hash(user.getPsw()));
                    u.addCategory(
                            new Category().edit(c -> c.setName("Задачи")));
                }));
    }

    @Override
    public User login(User user) throws ResourceNotFoundException, IncorrectPswException {
        return userRepository.saveAndFlush(
                userRepository.findByEmail(user.getEmail()).map(foundUser -> {
                    if (!Hash.check(user.getPsw(), foundUser.getPsw()))
                        throw new IncorrectPswException(user.getEmail(), user.getPsw());
                    return foundUser;
                }).orElseThrow(() -> new EmailNotExistsException(user.getEmail())));
    }

    @Override
    public User changeEmail(long userId, String newEmail) throws EmailExistsException, ResourceNotFoundException {
        if (userRepository.existsByEmail(newEmail))
            throw new EmailExistsException(newEmail);
        return userRepository.saveAndFlush(
                userRepository.findById(userId).map(user ->
                        user.edit(u -> u.setEmail(newEmail))
                ).orElseThrow(() -> new ResourceNotFoundException(User.class, userId)));
    }

    @Override
    public User changePsw(long userId, String newPsw) throws ResourceNotFoundException {
        return userRepository.saveAndFlush(
                userRepository.findById(userId).map(user ->
                        user.edit(u -> u.setPsw(Hash.hash(newPsw)))
                ).orElseThrow(() -> new ResourceNotFoundException(User.class, userId))
        );
    }

    @Override
    public User update(long userId, User newUser) throws ResourceNotFoundException {
        return userRepository.saveAndFlush(
                userRepository.findById(userId).map(user ->
                        user.edit(u -> {
                            u.setName(newUser.getName());
                            u.setSurname(newUser.getSurname());
                            u.setPatronymic(newUser.getPatronymic());
                            u.setBirthdate(newUser.getBirthdate());
                        })
                ).orElseThrow(() -> new ResourceNotFoundException(User.class, userId)));
    }

    @Override
    public void delete(long userId) {
        if (userRepository.existsById(userId))
            userRepository.deleteById(userId);
    }
}
