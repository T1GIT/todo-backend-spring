package com.todo.app.data.service;


import com.todo.app.data.util.exception.ResourceNotFoundException;
import com.todo.app.data.model.Category;

import java.util.List;


public interface CategoryService {

    List<Category> getOf(long userId)  throws ResourceNotFoundException;

    Category add(long userId, Category category) throws ResourceNotFoundException;

    Category changeName(long userId, long categoryId, String newName) throws ResourceNotFoundException;

    void delete(long userId, long categoryId);
}
