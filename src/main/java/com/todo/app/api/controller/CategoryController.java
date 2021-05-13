package com.todo.app.api.controller;

import com.todo.app.TodoApplication;
import com.todo.app.data.model.Category;
import com.todo.app.data.model.User;
import com.todo.app.data.service.CategoryService;
import com.todo.app.security.Auth;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Api(tags = "Category controller",
        description = "Controller to provide operations with category models")
@RestController
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Category> getCategoriesByUser(
            @AuthenticationPrincipal Auth authUser
    ) {
//        Auth authUser = (Auth) SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authUser);
        return categoryService.getOf(authUser.getId());
    }

//    @ResponseStatus(HttpStatus.CREATED)
//    @PostMapping(value = "/category", produces = MediaType.APPLICATION_JSON_VALUE)
//    public Category addCategory(
//            @RequestBody Category category) {
//        return categoryService.add(userId, category);
//    }
//
//    @ResponseStatus(HttpStatus.OK)
//    @PatchMapping(value = "/category/{categoryId}/name", produces = MediaType.APPLICATION_JSON_VALUE)
//    public Category changeName(
//            @PathVariable long categoryId,
//            @RequestBody Category category) {
//        return categoryService.changeName(categoryId, category.getName());
//    }
//
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @DeleteMapping(value = "/category/{categoryId}")
//    public void deleteCategory(
//            @PathVariable long categoryId) {
//        categoryService.delete(categoryId);
//    }
}