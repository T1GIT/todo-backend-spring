package com.todo.app.data.service;


import com.todo.app.data.exception.ResourceNotFoundException;
import com.todo.app.data.model.Category;
import com.todo.app.data.model.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CategoryService {

    List<Category> getOf(long userId)  throws ResourceNotFoundException;

    Category add(long userId, Category category) throws ResourceNotFoundException;

    void changeName(long categoryId, String newName) throws ResourceNotFoundException;

    void delete(long categoryId) throws ResourceNotFoundException;
}
