package com.todo.app.data.service.impl;

import com.todo.app.data.exception.ResourceNotFoundException;
import com.todo.app.data.model.Category;
import com.todo.app.data.model.Task;
import com.todo.app.data.repo.CategoryRepository;
import com.todo.app.data.repo.TaskRepository;
import com.todo.app.data.service.TaskService;
import com.todo.app.data.util.base.AbstractModel;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;


@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final CategoryRepository categoryRepository;
    private final TaskRepository taskRepository;

    public TaskServiceImpl(CategoryRepository categoryRepository, TaskRepository taskRepository) {
        this.categoryRepository = categoryRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> getOf(long categoryId) {
        return categoryRepository.findById(categoryId).map(category -> {
            List<Task> taskList = new ArrayList<>(category.getTasks());
            taskList.sort(Comparator.comparingLong(AbstractModel::getId));
            return taskList;
        }).orElseThrow(() -> new ResourceNotFoundException(Category.class, categoryId));
    }

    @Override
    public Task add(long categoryId, Task task) {
        return taskRepository.saveAndFlush(
            categoryRepository.findById(categoryId).map(
                    category -> task.edit(category::addTask)
            ).orElseThrow(() -> new ResourceNotFoundException(Category.class, categoryId)));
    }

    @Override
    public Task update(long taskId, Consumer<Task> editor) {
        return taskRepository.saveAndFlush(
                taskRepository.findById(taskId).map(
                        task -> task.edit(editor)
                ).orElseThrow(() -> new ResourceNotFoundException(Task.class, taskId)));
    }

    @Deprecated
    @Override
    public Task changeCategory(long taskId, long newCategoryId) {
        return taskRepository.saveAndFlush(
                categoryRepository.findById(newCategoryId).map(
                        category -> taskRepository.findById(taskId).map(
                                task -> task.edit(category::addTask)
                        ).orElseThrow(() -> new ResourceNotFoundException(Task.class, taskId))
                ).orElseThrow(() -> new ResourceNotFoundException(Category.class, newCategoryId)));
    }

    @Override
    public void delete(long taskId) {
        taskRepository.delete(
                taskRepository.findById(taskId)
                        .orElseThrow(() -> new ResourceNotFoundException(Task.class, taskId)));
    }
}
