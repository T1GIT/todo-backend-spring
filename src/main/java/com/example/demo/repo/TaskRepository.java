package com.example.demo.repo;

import com.example.demo.model.Category;
import com.example.demo.model.Task;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUserIdAndCategoriesId(long userId, long categoryId);

    List<Task> findAllByUser(User user);
}
