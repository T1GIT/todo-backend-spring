package com.todo.app.data.service.impl;

import com.todo.app.data.model.Category;
import com.todo.app.data.model.User;
import com.todo.app.data.repo.CategoryRepository;
import com.todo.app.data.repo.UserRepository;
import com.todo.app.data.service.CategoryService;
import com.todo.app.data.util.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@RequiredArgsConstructor
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public List<Category> getOf(long userId) {
        checkUserById(userId);
        return categoryRepository.getAllByUserId(userId);
    }

    @Override
    public Category add(long userId, Category category) {
        checkUserById(userId);
        User user = userRepository.getOne(userId);
        category.setUser(user);
        return categoryRepository.saveAndFlush(category);
    }

    @Override
    public void changeName(long userId, long categoryId, String name) {
        checkUserAndCategoryById(userId, categoryId);
        Category category = categoryRepository.getOne(categoryId);
        category.setName(name);
        categoryRepository.saveAndFlush(category);
    }

    @Override
    public void delete(long userId, long categoryId) {
        checkUserAndCategoryById(userId, categoryId);
        categoryRepository.deleteById(categoryId);
    }

    private void checkUserById(long userId) throws ResourceNotFoundException {
        if (!userRepository.existsById(userId))
            throw new ResourceNotFoundException(User.class, userId);
    }

    private void checkUserAndCategoryById(long userId, long categoryId) throws ResourceNotFoundException {
        checkUserById(userId);
        if (!categoryRepository.existsByUserIdAndId(userId, categoryId))
            throw new ResourceNotFoundException(User.class, userId, Category.class, categoryId);
    }
}
