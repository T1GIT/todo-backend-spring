package com.todo.app.data.service.impl;

import com.todo.app.data.model.Category;
import com.todo.app.data.model.Session;
import com.todo.app.data.model.User;
import com.todo.app.data.repo.CategoryRepository;
import com.todo.app.data.repo.SessionRepository;
import com.todo.app.data.repo.UserRepository;
import com.todo.app.data.service.UserService;
import com.todo.app.data.util.exception.EmailExistsException;
import com.todo.app.data.util.exception.EmailNotExistsException;
import com.todo.app.data.util.exception.ResourceNotFoundException;
import com.todo.app.security.crypt.Hash;
import com.todo.app.security.util.enums.Role;
import com.todo.app.security.util.exception.InvalidPswException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final SessionRepository sessionRepository;

    @Override
    public User register(User user) throws EmailExistsException {
        checkAbsenceUserByEmail(user.getEmail());
        user.setRole(Role.BASIC);
        user.setPsw(Hash.hash(user.getPsw()));
        userRepository.saveAndFlush(user);
        Category category = new Category();
        category.setName("Задачи");
        category.setUser(user);
        categoryRepository.saveAndFlush(category);
        return user;
    }

    @Override
    public User login(User user) throws ResourceNotFoundException, InvalidPswException {
        checkUserByEmail(user.getEmail());
        User foundUser = userRepository.getByEmail(user.getEmail());
        checkPassword(user.getPsw(), foundUser.getPsw());
        return foundUser;
    }

    @Override
    public void changeEmail(long userId, String email) throws EmailExistsException, ResourceNotFoundException {
        checkUserById(userId);
        checkAbsenceUserByEmail(email);
        User user = userRepository.getOne(userId);
        user.setEmail(email);
        userRepository.saveAndFlush(user);
    }

    @Override
    public void changePsw(long userId, String psw) throws ResourceNotFoundException {
        checkUserById(userId);
        User user = userRepository.getOne(userId);
        user.setPsw(Hash.hash(psw));
        userRepository.saveAndFlush(user);
    }

    @Override
    public void update(long userId, User user) throws ResourceNotFoundException {
        checkUserById(userId);
        User foundUser = userRepository.getOne(userId);
        if (user.getName() != null)
            foundUser.setName(user.getName());
        if (user.getSurname() != null)
            foundUser.setSurname(user.getSurname());
        if (user.getPatronymic() != null)
            foundUser.setPatronymic(user.getPatronymic());
        if (user.getBirthdate() != null)
            foundUser.setBirthdate(user.getBirthdate());
        userRepository.saveAndFlush(foundUser);
    }

    @Override
    public void delete(long userId) {
        if (userRepository.existsById(userId)) {
            categoryRepository.deleteAllByUserId(userId);
            sessionRepository.deleteAllByUserId(userId);
            userRepository.deleteById(userId);
        }
    }

    private void checkUserById(long userId) throws ResourceNotFoundException {
        if (!userRepository.existsById(userId))
            throw new ResourceNotFoundException(User.class, userId);
    }

    private void checkUserByEmail(String email) throws ResourceNotFoundException {
        if (!userRepository.existsByEmail(email))
            throw new ResourceNotFoundException(User.class, "email", email);
    }

    private void checkAbsenceUserByEmail(String email) throws EmailExistsException {
        if (userRepository.existsByEmail(email))
            throw new EmailExistsException(email);
    }

    private void checkPassword(String psw, String hash) throws InvalidPswException {
        if (!Hash.check(psw, hash))
            throw new InvalidPswException(psw);
    }
}
