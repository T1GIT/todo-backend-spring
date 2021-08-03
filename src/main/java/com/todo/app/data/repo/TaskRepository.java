package com.todo.app.data.repo;

import com.todo.app.data.model.Category;
import com.todo.app.data.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> getAllByCategoryId(long categoryId);

    boolean existsByCategoryIdAndId(long categoryId, long taskId);
}
