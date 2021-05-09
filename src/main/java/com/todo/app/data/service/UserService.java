package com.todo.app.data.service;


import com.todo.app.data.exception.IncorrectPswException;
import com.todo.app.data.exception.EmailExistsException;
import com.todo.app.data.exception.ResourceNotFoundException;
import com.todo.app.data.exception.UserNotFoundException;
import com.todo.app.data.model.User;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;


public interface UserService {

    User register(User user) throws EmailExistsException;

    User login(User user) throws UserNotFoundException, IncorrectPswException;

    void changeEmail(long userId, String newEmail) throws EmailExistsException, ResourceNotFoundException;

    void changePsw(long userId, String psw) throws ResourceNotFoundException;

    User update(long userId, Consumer<User> editor) throws ResourceNotFoundException;

    void delete(long userId) throws ResourceNotFoundException;
}
