package com.todo.app.api.controller.impl;

import com.todo.app.api.config.SwaggerConfig;
import com.todo.app.data.model.Category;
import com.todo.app.data.service.CategoryService;
import com.todo.app.security.auth.AuthContext;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


@Tag(name = "Category controller",
        description = "Controller to provide operations with category models")
@SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME)
@RequiredArgsConstructor
@RestController
@RequestMapping("/todo")
public class CategoryControllerImpl {

    private final CategoryService categoryService;
    private final AuthContext authContext;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Category> getCategoriesByUser() {
        return categoryService.getOf(authContext.getUser().getId());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/category", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addCategory(
            @RequestBody Category category) throws URISyntaxException {
        categoryService.add(authContext.getUser().getId(), category);
        return ResponseEntity
                .created(new URI("category/" + category.getId()))
                .build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/category/{categoryId}/name", produces = MediaType.APPLICATION_JSON_VALUE)
    public void changeName(
            @PathVariable long categoryId,
            @RequestBody Category category) {
        categoryService.changeName(
                authContext.getUser().getId(),
                categoryId,
                category.getName());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/category/{categoryId}")
    public void deleteCategory(
            @PathVariable long categoryId) {
        categoryService.delete(
                authContext.getUser().getId(),
                categoryId);
    }
}