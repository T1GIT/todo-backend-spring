package com.todo.app.data.service.impl;

import com.todo.app.data.model.Session;
import com.todo.app.data.model.User;
import com.todo.app.data.repo.SessionRepository;
import com.todo.app.data.repo.UserRepository;
import com.todo.app.data.service.SessionService;
import com.todo.app.data.util.exception.ResourceNotFoundException;
import com.todo.app.security.crypt.KeyGenerator;
import com.todo.app.security.util.enums.KeyLength;
import com.todo.app.security.util.exception.InvalidFingerprintException;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@RequiredArgsConstructor
@Service
@Transactional
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    @Override
    public Session create(long userId, String fingerprint) throws ResourceNotFoundException {
        return sessionRepository.saveAndFlush(
                userRepository.findById(userId).map(user ->
                        new Session().edit(r -> {
                            r.setRefresh(genValue());
                            r.setFingerprint(fingerprint);
                            r.setUser(user);
                        })
                ).orElseThrow(() -> new ResourceNotFoundException(User.class, userId)));
    }

    @Override
    public Session update(String refresh, String fingerprint) throws ResourceNotFoundException, InvalidFingerprintException {
        return sessionRepository.findByRefresh(refresh).map(session -> {
            if (session.getFingerprint().equals(fingerprint)) {
                return sessionRepository.saveAndFlush(
                        session.edit(r -> {
                            r.setRefresh(genValue());
                            r.setUser(Hibernate.unproxy(r.getUser(), User.class));
                        }));
            } else {
                sessionRepository.delete(session);
                throw new InvalidFingerprintException(session, fingerprint);
            }
        }).orElseThrow(() -> new ResourceNotFoundException(Session.class, "value", refresh));
    }

    @Override
    public void delete(String refresh) throws ResourceNotFoundException {
        sessionRepository.findByRefresh(refresh).ifPresent(sessionRepository::delete);
    }

    private String genValue() {
        String value;
        do {
            value = KeyGenerator.string(KeyLength.REFRESH);
        } while (sessionRepository.existsByRefresh(value));
        return value;
    }
}
