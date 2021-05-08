package com.todo.app.service.impl;

import com.todo.app.model.Category;
import com.todo.app.repo.CategoryRepository;
import com.todo.app.service.CategoryService;
import com.todo.app.utils.abstractService.impl.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class CategoryServiceImpl
        extends AbstractService<Category, CategoryRepository>
        implements CategoryService {

    public CategoryServiceImpl(CategoryRepository repository) {
        super(repository);
    }

}
