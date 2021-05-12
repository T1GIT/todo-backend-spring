package com.todo.app.data.repo;

import com.todo.app.data.model.Refresh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RefreshRepository extends JpaRepository<Refresh, Long> {

    Optional<Refresh> findByUserIdAndValue(long userId, String value);

    boolean existsByUserIdAndValue(long userId, String value);

    boolean existsByValue(String value);

    void deleteByValue(String value);
}
