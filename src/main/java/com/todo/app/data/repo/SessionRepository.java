package com.todo.app.data.repo;

import com.todo.app.data.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findByRefresh(String refresh);

    Session getByRefresh(String refresh);

    Optional<Session> findByFingerprint(String fingerprint);

    boolean existsByRefresh(String refresh);

    void deleteAllByUserIdAndFingerprint(long userId, String fingerprint);

    void deleteAllByUserId(long userId);

    void deleteByRefresh(String refresh);
}
