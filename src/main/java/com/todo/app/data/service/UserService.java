package com.todo.app.data.service;


import com.todo.app.data.model.User;
import com.todo.app.data.util.exception.EmailExistsException;
import com.todo.app.data.util.exception.EmailNotExistsException;
import com.todo.app.data.util.exception.ResourceNotFoundException;
import com.todo.app.security.util.exception.InvalidPswException;


public interface UserService {

    User register(User user) throws EmailExistsException;

    User login(User user) throws EmailNotExistsException, InvalidPswException;

    void changeEmail(long userId, String email) throws EmailExistsException, ResourceNotFoundException;

    void changePsw(long userId, String psw) throws ResourceNotFoundException;

    void update(long userId, User user) throws ResourceNotFoundException;

    void delete(long userId) throws ResourceNotFoundException;
}
