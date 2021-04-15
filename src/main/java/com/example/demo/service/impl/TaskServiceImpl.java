package com.example.demo.service.impl;

import com.example.demo.model.Category;
import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.repo.TaskRepository;
import com.example.demo.service.TaskService;
import com.example.demo.utils.abstractService.impl.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class TaskServiceImpl
        extends AbstractService<Task, TaskRepository>
        implements TaskService {

    public TaskServiceImpl(TaskRepository repository) {
        super(repository);
    }

    @Override
    public List<Task> getByUserAndCategory(User user, Category category) {
        return repository.findAllByUserIdAndCategoriesId(user.getId(), category.getId());
    }

    @Override
    public List<Task> getByUser(User user) {
        return repository.findAllByUser(user);
    }
}
