package com.todo.app.data.service.impl;

import com.todo.app.data.util.exception.ResourceNotFoundException;
import com.todo.app.data.model.Category;
import com.todo.app.data.model.User;
import com.todo.app.data.repo.CategoryRepository;
import com.todo.app.data.repo.UserRepository;
import com.todo.app.data.service.CategoryService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Category> getOf(long userId) throws ResourceNotFoundException {
        return userRepository.findById(userId).map(user -> {
            List<Category> categoryList = new ArrayList<>(user.getCategories());
            categoryList.sort((c1, c2) ->
                    String.CASE_INSENSITIVE_ORDER.compare(c1.getName(), c2.getName()));
            return categoryList;
        }).orElseThrow(() -> new ResourceNotFoundException(User.class, userId));
    }

    @Override
    public Category add(long userId, Category category) throws ResourceNotFoundException {
        return categoryRepository.saveAndFlush(
                userRepository.findById(userId).map(
                        user -> category.edit(user::addCategory)
                ).orElseThrow(() -> new ResourceNotFoundException(User.class, userId)));
    }

    @Override
    public Category changeName(long categoryId, String newName) throws ResourceNotFoundException {
        return categoryRepository.saveAndFlush(
                categoryRepository.findById(categoryId).map(category -> category
                        .edit(c -> c.setName(newName))
                ).orElseThrow(() -> new ResourceNotFoundException(Category.class, categoryId)));
    }

    @Override
    public void delete(long categoryId) {
        if (categoryRepository.existsById(categoryId))
            categoryRepository.deleteById(categoryId);
    }
}
