package com.todo.app.data.service.impl;

import com.todo.app.data.model.User;
import com.todo.app.data.repo.UserRepository;
import com.todo.app.data.service.AdminService;
import com.todo.app.data.util.exception.ResourceNotFoundException;
import com.todo.app.security.util.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;


@RequiredArgsConstructor
@Component
@Transactional
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    @Override
    public void changeRole(long userId, Role role) {
        checkUserById(userId);
        User user = userRepository.getOne(userId);
        user.setRole(role);
        userRepository.saveAndFlush(user);
    }

    private void checkUserById(long userId) throws ResourceNotFoundException {
        if (!userRepository.existsById(userId))
            throw new ResourceNotFoundException(User.class, userId);
    }
}
