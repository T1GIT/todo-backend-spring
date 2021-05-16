package com.todo.app.data.service;


import com.todo.app.data.util.exception.EmailNotExistsException;
import com.todo.app.data.util.exception.EmailExistsException;
import com.todo.app.data.util.exception.ResourceNotFoundException;
import com.todo.app.data.model.User;
import com.todo.app.security.util.exception.IncorrectPswException;


public interface UserService {

    User register(User user) throws EmailExistsException;

    User login(User user) throws EmailNotExistsException, IncorrectPswException;

    void changeEmail(long userId, String newEmail) throws EmailExistsException, ResourceNotFoundException;

    void changePsw(long userId, String newPsw) throws ResourceNotFoundException;

    void update(long userId, User newUser) throws ResourceNotFoundException;

    void delete(long userId);
}
