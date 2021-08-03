package com.todo.app.data.service.impl;

import com.todo.app.data.model.Category;
import com.todo.app.data.model.Task;
import com.todo.app.data.model.User;
import com.todo.app.data.repo.CategoryRepository;
import com.todo.app.data.repo.TaskRepository;
import com.todo.app.data.repo.UserRepository;
import com.todo.app.data.service.TaskService;
import com.todo.app.data.util.base.AbstractModel;
import com.todo.app.data.util.exception.NotOwnerException;
import com.todo.app.data.util.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TaskRepository taskRepository;

    @Override
    public List<Task> getOf(long userId, long categoryId) {
        checkUserAndCategoryById(userId, categoryId);
        return taskRepository.getAllByCategoryId(categoryId);
    }

    @Override
    public Task add(long userId, long categoryId, Task task) {
        checkUserAndCategoryById(userId, categoryId);
        Category category = categoryRepository.getOne(categoryId);
        task.setCategory(category);
        taskRepository.saveAndFlush(task);
        return task;
    }

    @Override
    public void update(long userId, long categoryId, long taskId, Task task) {
        checkUserAndCategoryAndTaskById(userId, categoryId, taskId);
        Task foundTask = taskRepository.getOne(taskId);
        if (task.getTitle() != null)
            foundTask.setTitle(task.getTitle());
        if (task.getDescription() != null)
            foundTask.setDescription(task.getDescription());
        taskRepository.saveAndFlush(foundTask);
    }

    @Override
    public void setCompleted(long userId, long categoryId, long taskId, boolean isCompleted) throws ResourceNotFoundException {
        checkUserAndCategoryAndTaskById(userId, categoryId, taskId);
        Task task = taskRepository.getOne(taskId);
        if (isCompleted) {
            task.setCompleted(true);
            task.setExecuteDate(new Date());
        } else {
            task.setCompleted(false);
            task.setExecuteDate(null);
        }
        taskRepository.saveAndFlush(task);
    }

    @Override
    public void delete(long userId, long categoryId, long taskId) {
        checkUserAndCategoryAndTaskById(userId, categoryId, taskId);
        taskRepository.deleteById(taskId);
    }

    private void checkUserAndCategoryById(long userId, long categoryId) throws ResourceNotFoundException {
        if (!userRepository.existsById(userId))
            throw new ResourceNotFoundException(User.class, userId);
        if (!categoryRepository.existsByUserIdAndId(userId, categoryId))
            throw new ResourceNotFoundException(User.class, userId, Category.class, categoryId);
    }

    private void checkUserAndCategoryAndTaskById(long userId, long categoryId, long taskId) throws ResourceNotFoundException {
        checkUserAndCategoryById(userId, categoryId);
        if (!taskRepository.existsByCategoryIdAndId(categoryId, taskId))
            throw new ResourceNotFoundException(Category.class, categoryId, Task.class, taskId);
    }
}
