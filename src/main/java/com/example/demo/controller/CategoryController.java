package com.example.demo.controller;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Category;
import com.example.demo.model.Category;
import com.example.demo.model.Category;
import com.example.demo.model.User;
import com.example.demo.service.CategoryService;
import com.example.demo.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public Page<Category> getCategories(Pageable pageable) {
        return categoryService.getAll(pageable);
    }

    @GetMapping("/categories/{categoryId}")
    public Category getCategoryById(@PathVariable long categoryId) {
        return categoryService.getById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + categoryId));
    }

    @PostMapping("/categories")
    public Category createCategory(@Valid @RequestBody Category category) {
        return categoryService.add(category);
    }

    @PutMapping("/categories/{categoryId}")
    public Category addCategory(@PathVariable long categoryId, @Valid @RequestBody Category requestCategory) {
        return categoryService.getById(categoryId)
                .map(category -> {
                    category.setName(requestCategory.getName());
                    return categoryService.update(category);
                }).orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + categoryId));
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable long categoryId) {
        return categoryService.getById(categoryId)
                .map(category -> {
                    categoryService.delete(category);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + categoryId));
    }
}