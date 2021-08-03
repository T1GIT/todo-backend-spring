package com.todo.app.api.controller.impl;

import com.todo.app.data.model.Category;
import com.todo.app.data.service.CategoryService;
import com.todo.app.security.auth.AuthContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;


@RequiredArgsConstructor
@RestController
public class CategoryControllerImpl implements com.todo.app.api.controller.CategoryController {

    private final CategoryService categoryService;
    private final AuthContext authContext;

    @Override
    public List<Category> getCategoriesByUser() {
        return categoryService.getOf(authContext.getUser().getId());
    }

    @Override
    public ResponseEntity<Void> addCategory(Category category) {
        categoryService.add(authContext.getUser().getId(), category);
        return ResponseEntity
                .created(URI.create("categories/" + category.getId()))
                .build();
    }

    @Override
    public void changeName(long categoryId, Category category) {
        categoryService.changeName(
                authContext.getUser().getId(),
                categoryId,
                category.getName());
    }

    @Override
    public void deleteCategory(long categoryId) {
        categoryService.delete(
                authContext.getUser().getId(),
                categoryId);
    }
}