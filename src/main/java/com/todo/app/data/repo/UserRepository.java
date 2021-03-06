package com.todo.app.data.repo;

import com.todo.app.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    User getByEmail(String email);

    boolean existsByEmail(String email);
}
