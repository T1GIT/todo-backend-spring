package com.example.demo.service.impl;

import com.example.demo.model.Category;
import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.repo.CategoryRepository;
import com.example.demo.repo.TaskRepository;
import com.example.demo.service.CategoryService;
import com.example.demo.service.TaskService;
import com.example.demo.utils.abstractService.impl.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class CategoryServiceImpl
        extends AbstractService<Category, CategoryRepository>
        implements CategoryService {

    public CategoryServiceImpl(CategoryRepository repository) {
        super(repository);
    }

}
