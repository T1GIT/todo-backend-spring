package com.todo.app.data.repo;

import com.todo.app.data.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findByRefresh(String value);

    boolean existsByRefresh(String value);

    void deleteByRefresh(String value);
}
