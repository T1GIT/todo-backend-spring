package com.todo.app.data.service.impl;

import com.todo.app.data.service.AdminService;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.List;


@Component
public class AdminServiceImpl implements AdminService {

    private final EntityManager entityManager;

    public AdminServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public List<Object> executeSql(String query) {
        return entityManager.createNativeQuery(query).getResultList();
    }
}
