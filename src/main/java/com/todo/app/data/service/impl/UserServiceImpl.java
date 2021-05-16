package com.todo.app.data.service.impl;

import com.todo.app.data.util.exception.EmailExistsException;
import com.todo.app.data.util.exception.EmailNotExistsException;
import com.todo.app.data.util.exception.ResourceNotFoundException;
import com.todo.app.data.model.Category;
import com.todo.app.data.model.User;
import com.todo.app.data.repo.UserRepository;
import com.todo.app.data.service.UserService;
import com.todo.app.security.crypt.Hash;
import com.todo.app.security.util.enums.Role;
import com.todo.app.security.util.exception.IncorrectPswException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User register(User user) throws EmailExistsException {
        if (userRepository.existsByEmail(user.getEmail()))
            throw new EmailExistsException(user.getEmail());
        return userRepository.saveAndFlush(
                user.edit(u -> {
                    u.setPsw(Hash.hash(user.getPsw()));
                    u.addCategory(new Category() {{ setName("Задачи"); }});
                    u.setRole(Role.BASIC);
                }));
    }

    @Override
    public User login(User user) throws ResourceNotFoundException, IncorrectPswException {
        return userRepository.saveAndFlush(
                userRepository.findByEmail(user.getEmail()).map(foundUser -> {
                    if (!Hash.check(user.getPsw(), foundUser.getPsw()))
                        throw new IncorrectPswException(user.getEmail());
                    return foundUser;
                }).orElseThrow(() -> new EmailNotExistsException(user.getEmail())));
    }

    @Override
    public void changeEmail(long userId, String newEmail) throws EmailExistsException, ResourceNotFoundException {
        if (userRepository.existsByEmail(newEmail))
            throw new EmailExistsException(newEmail);
        userRepository.saveAndFlush(
                userRepository.findById(userId).map(user ->
                        user.edit(u -> u.setEmail(newEmail))
                ).orElseThrow(() -> new ResourceNotFoundException(User.class, userId)));
    }

    @Override
    public void changePsw(long userId, String newPsw) throws ResourceNotFoundException {
        userRepository.saveAndFlush(
                userRepository.findById(userId).map(user ->
                        user.edit(u -> u.setPsw(Hash.hash(newPsw)))
                ).orElseThrow(() -> new ResourceNotFoundException(User.class, userId))
        );
    }

    @Override
    public void update(long userId, User newUser) throws ResourceNotFoundException {
        userRepository.saveAndFlush(
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
        userRepository.findById(userId).ifPresent(userRepository::delete);
    }
}
