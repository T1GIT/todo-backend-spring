package com.todo.app.service.impl;

import com.todo.app.model.Category;
import com.todo.app.model.Task;
import com.todo.app.model.User;
import com.todo.app.repo.TaskRepository;
import com.todo.app.service.TaskService;
import com.todo.app.utils.abstractService.impl.AbstractService;
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
