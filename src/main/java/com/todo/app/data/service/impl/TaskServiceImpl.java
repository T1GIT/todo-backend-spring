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
            categoryRepository.findById(categoryId).map(category ->
                    task.edit(category::addTask)
            ).orElseThrow(() -> new ResourceNotFoundException(Category.class, categoryId)));
    }

    @Override
    public Task update(long taskId, Task newTask) {
        return taskRepository.saveAndFlush(
                taskRepository.findById(taskId).map(task ->
                        task.edit(t -> {
                            t.setTitle(newTask.getTitle());
                            t.setDescription(newTask.getDescription());
                        })
                ).orElseThrow(() -> new ResourceNotFoundException(Task.class, taskId)));
    }

    @Override
    public Task setCompleted(long taskId, boolean isCompleted) throws ResourceNotFoundException {
        return taskRepository.saveAndFlush(
                taskRepository.findById(taskId).map(task ->
                        task.edit(t -> {
                            if (isCompleted) {
                                t.setCompleted(true);
                                t.setExecuteDate(new Date());
                            } else {
                                t.setCompleted(false);
                                t.setExecuteDate(null);
                            }
                        })
                ).orElseThrow(() -> new ResourceNotFoundException(Task.class, taskId)));
    }

    @Override
    public void delete(long taskId) {
        taskRepository.findById(taskId).ifPresent(taskRepository::delete);
    }
}
