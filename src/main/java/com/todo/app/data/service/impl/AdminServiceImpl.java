package com.todo.app.data.service.impl;

import com.todo.app.data.model.User;
import com.todo.app.data.repo.UserRepository;
import com.todo.app.data.service.AdminService;
import com.todo.app.data.util.exception.ResourceNotFoundException;
import com.todo.app.security.util.enums.Role;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.List;


@Component
public class AdminServiceImpl implements AdminService {

    private final EntityManager entityManager;
    private final UserRepository userRepository;

    public AdminServiceImpl(EntityManager entityManager, UserRepository userRepository) {
        this.entityManager = entityManager;
        this.userRepository = userRepository;
    }

    @Override
    public List<Object> executeSql(String query) {
        System.out.println(entityManager.createNativeQuery(query).getClass());
        return entityManager.createNativeQuery(query).getResultList();
    }

    @Override
    public User changeRole(long userId, Role role) {
        return userRepository.saveAndFlush(
                userRepository.findById(userId).map(user ->
                        user.edit(u -> u.setRole(role))
                ).orElseThrow(() -> new ResourceNotFoundException(User.class, userId)));
    }
}
