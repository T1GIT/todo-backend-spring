package com.todo.app.data.service;


import com.todo.app.data.model.Category;
import com.todo.app.data.util.exception.NotOwnerException;
import com.todo.app.data.util.exception.ResourceNotFoundException;

import java.util.List;


public interface CategoryService {

    List<Category> getOf(long userId)  throws ResourceNotFoundException;

    Category add(long userId, Category category) throws ResourceNotFoundException;

    void changeName(long userId, long categoryId, String newName) throws ResourceNotFoundException, NotOwnerException;

    void delete(long userId, long categoryId) throws ResourceNotFoundException, NotOwnerException;
}
