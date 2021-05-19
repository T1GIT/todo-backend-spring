package com.todo.app.data.service.impl;

import com.todo.app.data.model.Session;
import com.todo.app.data.model.User;
import com.todo.app.data.repo.SessionRepository;
import com.todo.app.data.repo.UserRepository;
import com.todo.app.data.service.SessionService;
import com.todo.app.data.util.exception.ResourceNotFoundException;
import com.todo.app.security.crypt.KeyGenerator;
import com.todo.app.security.token.RefreshProvider;
import com.todo.app.security.util.enums.KeyLength;
import com.todo.app.security.util.exception.ExpiredRefreshException;
import com.todo.app.security.util.exception.InvalidFingerprintException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;


@RequiredArgsConstructor
@Service
@Transactional
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    @Override
    public Session create(long userId, String fingerprint) throws ResourceNotFoundException {
        return sessionRepository.saveAndFlush(
                userRepository.findById(userId).map(user -> {
                    sessionRepository.findByFingerprint(fingerprint).ifPresent(sessionRepository::delete);
                    return new Session().edit(s -> {
                        s.setRefresh(genValue());
                        s.setExpires(new Date(System.currentTimeMillis() + RefreshProvider.DURATION.toMillis()));
                        s.setFingerprint(fingerprint);
                        user.addSession(s);
                    });
                }).orElseThrow(() -> new ResourceNotFoundException(User.class, userId)));
    }

    @Override
    public Session update(String refresh, String fingerprint) {
        return sessionRepository.findByRefresh(refresh).map(session -> {
            if (session.getExpires().compareTo(new Date()) < 0)
                throw new ExpiredRefreshException(session.getId(), session.getRefresh());
            if (!session.getFingerprint().equals(fingerprint))
                throw new InvalidFingerprintException(session, fingerprint);
            return sessionRepository.saveAndFlush(
                    session.edit(s -> {
                        s.setRefresh(genValue());
                        s.setExpires(new Date(System.currentTimeMillis() + RefreshProvider.DURATION.toMillis()));
                    }));
        }).orElseThrow(() -> new ResourceNotFoundException(Session.class, "value", refresh));
    }

    @Override
    public void delete(String refresh) {
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
