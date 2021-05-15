package com.todo.app.data.service.impl;

import com.todo.app.data.model.User;
import com.todo.app.data.repo.UserRepository;
import com.todo.app.data.service.AdminService;
import com.todo.app.data.util.exception.ResourceNotFoundException;
import com.todo.app.security.util.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;


@RequiredArgsConstructor
@Component
@Transactional
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    @Override
    public User changeRole(long userId, Role role) {
        return userRepository.saveAndFlush(
                userRepository.findById(userId).map(user ->
                        user.edit(u -> u.setRole(role))
                ).orElseThrow(() -> new ResourceNotFoundException(User.class, userId)));
    }
}
