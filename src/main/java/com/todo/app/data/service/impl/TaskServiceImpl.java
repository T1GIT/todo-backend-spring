package com.todo.app.data.service.impl;

import com.todo.app.data.util.exception.ResourceNotFoundException;
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
import java.util.Date;
import java.util.List;


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
    public List<Task> getOf(long userId, long categoryId) {
        return categoryRepository.findById(categoryId).map(category -> {
            List<Task> taskList = new ArrayList<>(category.getTasks());
            taskList.sort(Comparator.comparingLong(AbstractModel::getId));
            return taskList;
        }).orElseThrow(() -> new ResourceNotFoundException(Category.class, categoryId));
    }

    @Override
    public Task add(long userId, long categoryId, Task task) {
        return taskRepository.saveAndFlush(
            categoryRepository.findByUserIdAndId(userId, categoryId).map(category ->
                    task.edit(t -> t.setCategory(category))
            ).orElseThrow(() -> new ResourceNotFoundException(Category.class, categoryId)));
    }

    @Override
    public void update(long userId, long taskId, Task newTask) {
        taskRepository.saveAndFlush(
                taskRepository.findById(taskId).map(task -> {
                    if (!categoryRepository.existsByUserIdAndId(userId, task.getCategory().getId()))
                        throw new ResourceNotFoundException(Category.class, "taskId", taskId);
                    return task.edit(t -> {
                        t.setTitle(newTask.getTitle());
                        t.setDescription(newTask.getDescription());
                    });
                }).orElseThrow(() -> new ResourceNotFoundException(Task.class, taskId))
        );
    }

    @Override
    public void setCompleted(long userId, long taskId, boolean isCompleted) throws ResourceNotFoundException {
        taskRepository.saveAndFlush(
                taskRepository.findById(taskId).map(task -> {
                    if (!categoryRepository.existsByUserIdAndId(userId, task.getCategory().getId()))
                        throw new ResourceNotFoundException(Category.class, "taskId", taskId);
                    return task.edit(t -> {
                        if (isCompleted) {
                            t.setCompleted(true);
                            t.setExecuteDate(new Date());
                        } else {
                            t.setCompleted(false);
                            t.setExecuteDate(null);
                        }
                    });
                }).orElseThrow(() -> new ResourceNotFoundException(Task.class, taskId))
        );
    }

    @Override
    public void delete(long userId, long taskId) {
        taskRepository.findById(taskId).ifPresent(task -> {
            if (!categoryRepository.existsByUserIdAndId(userId, task.getCategory().getId()))
                throw new ResourceNotFoundException(Category.class, "taskId", taskId);
            taskRepository.delete(task);
        });
    }
}
