package com.todo.app.data.service.impl;

import com.todo.app.data.model.Category;
import com.todo.app.data.model.User;
import com.todo.app.data.repo.CategoryRepository;
import com.todo.app.data.repo.UserRepository;
import com.todo.app.data.service.CategoryService;
import com.todo.app.data.util.exception.NotOwnerException;
import com.todo.app.data.util.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public List<Category> getOf(long userId) {
        return userRepository.findById(userId).map(user ->
                user.getCategories().stream()
                        .map(c -> Hibernate.unproxy(c, Category.class))
                        .sorted((c1, c2) -> String.CASE_INSENSITIVE_ORDER.compare(c1.getName(), c2.getName()))
                        .collect(Collectors.toList())
        ).orElseThrow(() -> new ResourceNotFoundException(User.class, userId));
    }

    @Override
    public Category add(long userId, Category category) {
        return categoryRepository.saveAndFlush(
                userRepository.findById(userId).map(user ->
                        category.edit(user::addCategory)
                ).orElseThrow(() -> new ResourceNotFoundException(User.class, userId)));
    }

    @Override
    public void changeName(long userId, long categoryId, String newName) {
        categoryRepository.saveAndFlush(
                categoryRepository.findById(categoryId).map(category -> {
                    if (category.getUser().getId() != userId)
                        throw new NotOwnerException(userId, Category.class, categoryId);
                    return category.edit(c -> c.setName(newName));
                }).orElseThrow(() -> new ResourceNotFoundException(Category.class, categoryId)));
    }

    @Override
    public void delete(long userId, long categoryId) {
        categoryRepository.findById(categoryId)
                .ifPresentOrElse(category -> {
                    if (category.getUser().getId() != userId)
                        throw new NotOwnerException(userId, Category.class, categoryId);
                    categoryRepository.delete(category);
                }, () -> {
                    throw new ResourceNotFoundException(Category.class, categoryId);
                });
    }
}
