package com.todo.app.data.repo;

import com.todo.app.data.model.Category;
import com.todo.app.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> getAllByUserId(long userId);

    boolean existsByUserIdAndId(long userId, long categoryId);

    void deleteAllByUserId(long userId);
}

